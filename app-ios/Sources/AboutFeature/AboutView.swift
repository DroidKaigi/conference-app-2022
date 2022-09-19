import ComposableArchitecture
import SwiftUI
import Strings
import Theme

public struct AboutState: Equatable {
    public init() {}
}

public enum AboutAction {
    case openAccess
    case openStaffs
    case openPrivacyPolicy
    case openLicense
}

public struct AboutEnvironment {
    @Environment(\.openURL) var openURL

    struct StaticURLs {
        static let privacyPolicy = URL(string: "https://portal.droidkaigi.jp/about/privacy")!
    }
    public init() {}
}

public let aboutReducer = Reducer<AboutState, AboutAction, AboutEnvironment> { _, action, environment in
    switch action {
    case .openAccess:
        return .none
    case .openStaffs:
        return .none
    case .openPrivacyPolicy:
        environment.openURL(AboutEnvironment.StaticURLs.privacyPolicy)
        return .none
    case .openLicense:
        return .none
    }
}

public struct AboutView: View {
    private let store: Store<AboutState, AboutAction>

    public init(store: Store<AboutState, AboutAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            ScrollView {
                AboutViewAssets.logoCharacter.swiftUIImage
                VStack(alignment: .leading, spacing: 24) {
                    Text(L10n.About.whatIsDroidKaigi)
                        .font(Font.system(size: 32, weight: .medium))
                    Text(L10n.About.description)
                    HStack(spacing: 16) {
                        AboutViewAssets.twitter.swiftUIImage
                        AboutViewAssets.youtube.swiftUIImage
                        AboutViewAssets.medium.swiftUIImage
                        Spacer()
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 32)

                Divider()
                    .background(AssetColors.outline.swiftUIColor)
                    .padding(.horizontal, 32)

                ForEach(AboutNavigationItem.items, id: \.title) { item in
                    Button {
                        viewStore.send(item.action)
                    } label: {
                        HStack(spacing: 12) {
                            item.image.swiftUIImage
                                .renderingMode(.template)
                            Text(item.title)
                            Spacer()
                        }
                        .padding(16)
                        .frame(minHeight: 56)
                    }
                }

                Spacer()
                    .frame(height: 32)
            }
            .foregroundColor(AssetColors.onBackground.swiftUIColor)
            .background(AssetColors.background.swiftUIColor)
        }
    }
}

#if DEBUG
struct AboutView_Previews: PreviewProvider {
    static var previews: some View {
        AboutView(
            store: .init(
                initialState: .init(),
                reducer: .empty,
                environment: AboutEnvironment()
            )
        )
        .preferredColorScheme(.dark)
    }
}
#endif
