import appioscombined
import ComposableArchitecture
import SwiftUI
import Theme

public struct AboutState: Equatable {
    public enum NavigationDestination {
        case none
        case license

        @ViewBuilder
        public var destination: some View {
            switch self {
            case .license:
                AboutLicenseView()
            default:
                EmptyView()
            }
        }
    }

    public var navigationDestination: NavigationDestination

    public init(
        navigationDestination: NavigationDestination = .none
    ) {
        self.navigationDestination = navigationDestination
    }
}

public enum AboutAction {
    case backToTop
    case openAccess
    case openStaffs
    case openPrivacyPolicy
    case openLicense
}

public struct AboutEnvironment {
    public init() {}
}

public let aboutReducer = Reducer<AboutState, AboutAction, AboutEnvironment> { state, action, _ in
    switch action {
    case .backToTop:
        state.navigationDestination = .none
        return .none
    case .openLicense:
        state.navigationDestination = .license
        return .none
    default:
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
            NavigationView {
                ScrollView {
                    AboutViewAssets.logoCharacter.swiftUIImage
                    VStack(alignment: .leading, spacing: 24) {
                        Text(StringsKt.shared.about_title.desc().localized())
                            .font(Font.system(size: 32, weight: .medium))
                        Text(StringsKt.shared.about_description.desc().localized())
                        HStack(spacing: 16) {
                            LinkImage(
                                image: AboutViewAssets.twitter.swiftUIImage,
                                url: URL(string: StaticURLs.twitter)!
                            )
                            LinkImage(
                                image: AboutViewAssets.youtube.swiftUIImage,
                                url: URL(string: StaticURLs.youtube)!
                            )
                            LinkImage(
                                image: AboutViewAssets.medium.swiftUIImage,
                                url: URL(string: StaticURLs.medium)!
                            )
                            Spacer()
                        }
                    }
                    .padding(.horizontal, 32)
                    .padding(.vertical, 32)

                    Divider()
                        .background(AssetColors.outline.swiftUIColor)
                        .padding(.horizontal, 45)

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
                    .padding(.horizontal, 29)

                    ForEach(AboutTextItem.items, id: \.title) { item in
                        HStack {
                            Text(item.title)
                            Spacer()
                            Text(item.content)
                            Spacer()
                                .frame(width: 14)
                        }
                        .padding(16)
                        .frame(minHeight: 56)
                    }
                    .padding(.horizontal, 29)

                    Spacer()
                        .frame(height: 32)

                    NavigationLink(
                        destination: viewStore.navigationDestination.destination,
                        isActive: Binding<Bool>(
                            get: {
                                viewStore.navigationDestination != .none
                            }, set: { newValue in
                                if !newValue {
                                    viewStore.send(.backToTop)
                                }
                            }),
                        label: {
                            EmptyView()
                        }
                    )
                }
                .foregroundColor(AssetColors.onBackground.swiftUIColor)
                .background(AssetColors.background.swiftUIColor)
                .navigationBarHidden(true)
            }
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
