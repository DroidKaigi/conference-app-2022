import ComposableArchitecture
import Model
import Strings
import StaffFeature
import SwiftUI
import Theme

public enum AboutDestination {
    case access
    case staffs
    case privacyPolicy
    case license
}

public struct AboutState: Equatable {
    public var staffState: StaffState
    public var isSheetPresented: Bool
    public var destination: AboutDestination?
    public init(
        staffState: StaffState = .init(staffs: Staff.companion.fakes()),
        isSheetPresented: Bool = false,
        destination: AboutDestination? = nil
    ) {
        self.isSheetPresented = isSheetPresented
        self.staffState = staffState
        self.destination = destination
    }
}

public enum AboutAction {
    case openAccess
    case openStaffs
    case openPrivacyPolicy
    case openLicense
    case staff(StaffAction)
    case setSheet(isPresented: Bool)
}

public struct AboutEnvironment {
    public init() {}
}

public let aboutReducer = Reducer<AboutState, AboutAction, AboutEnvironment>.combine(
    staffReducer.pullback(
        state: \.staffState,
        action: /AboutAction.staff,
        environment: { _ in .init() }
    ),
    .init { state, action, _ in
        switch action {
        case .setSheet(isPresented: true):
            state.isSheetPresented = true
            return .none
        case .setSheet(isPresented: false):
            state.isSheetPresented = false
            return .none
        case .staff:
            return .none
        case .openAccess:
            state.destination = .access
            return .none
        case .openStaffs:
            state.destination = .staffs
            return .none
        case .openPrivacyPolicy:
            state.destination = .privacyPolicy
            return .none
        case .openLicense:
            state.destination  = .license
            return .none
        }
    }
)

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
                .padding(.horizontal, 16)
                .padding(.vertical, 32)

                Divider()
                    .background(AssetColors.outline.swiftUIColor)
                    .padding(.horizontal, 32)

                ForEach(AboutNavigationItem.items, id: \.title) { item in
                    Button {
                        viewStore.send(item.action)
                        viewStore.send(.setSheet(isPresented: true))
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
                    .sheet(isPresented: viewStore.binding(get: \.isSheetPresented, send: { value in
                            .setSheet(isPresented: value)
                    }), onDismiss: {
                        viewStore.send(.setSheet(isPresented: false))
                    }, content: {
                        if let destination = viewStore.state.destination {
                            switch destination {
                            case .access:
                                Text("TODO: Access View")
                            case .staffs:
                                StaffView(
                                    store: store.scope(state: \.staffState, action: AboutAction.staff)
                                )
                            case .privacyPolicy:
                                Text("TODO: Privacy Policy")
                            case .license:
                                Text("TODO: License")
                            }
                        }
                    })
                }

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
