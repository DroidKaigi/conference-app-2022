import appioscombined
import Assets
import CommonComponents
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct ContributorState: Equatable {
    public var contributors: [Contributor]

    public init(contributors: [Contributor] = []) {
        self.contributors = contributors
    }
}

public enum ContributorAction {
    case refresh
    case refreshResponse(TaskResult<[Contributor]>)
}
public struct ContributorEnvironment {
    public let contributorsRepository: ContributorsRepository

    public init(contributorsRepository: ContributorsRepository) {
        self.contributorsRepository = contributorsRepository
    }
}
public let contributorReducer = Reducer<ContributorState, ContributorAction, ContributorEnvironment> { state, action, environment in
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            try await environment.contributorsRepository.refresh()
            for try await result: [Contributor] in environment.contributorsRepository.contributors().stream() {
                await subscriber.send(
                    .refreshResponse(
                        TaskResult {
                            result
                        }
                    )
                )
            }
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case .refreshResponse(.success(let contributors)):
        state.contributors = contributors
        return .none
    case .refreshResponse(.failure):
        return .none
    }
}

public struct ContributorView: View {

    @Environment(\.openURL) var openURL

    private let store: Store<ContributorState, ContributorAction>

    public init(store: Store<ContributorState, ContributorAction>) {
        self.store = store

        UITabBar.appearance().barTintColor = AssetColors.surface.color
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            List(viewStore.contributors, id: \.id) { contributor in
                ContributorItemView(contributor: contributor, action: {
                    guard let profileUrl = contributor.profileUrl else { return }
                    if let url = URL(string: profileUrl) {
                        openURL(url)
                    }
                })
                .listRowInsets(EdgeInsets())
                .listRowBackground(AssetColors.background.swiftUIColor)
                .listRowSeparator(.hidden)
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
            .listStyle(PlainListStyle())
            .navigationTitle(StringsKt.shared.title_contributors.localized())
            .navigationBarTitleDisplayMode(.inline)
        }
    }
}

struct ContributorItemView: View {

    let contributor: Contributor
    var action: () -> Void

    var body: some View {
        ZStack {
            HStack(spacing: 16) {
                NetworkImage(url: URL(string: contributor.iconUrl)) {
                    AnyView(Color(.systemGray4))
                }
                .frame(width: 60, height: 60)
                .clipShape(Circle())
                Text(contributor.username)
                    .font(Font.system(size: 16, weight: .bold, design: .default))
                    .foregroundColor(AssetColors.onBackground.swiftUIColor)
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)

            Button {
                action()
            } label: {
                ZStack {
                    EmptyView()
                }
                .frame(minWidth: 0, maxWidth: .infinity)
                .frame(minHeight: 0, maxHeight: .infinity)
            }
            .buttonStyle(ListItemButtonStyle())
        }
    }
}

struct ListItemButtonStyle: ButtonStyle {
    func makeBody(configuration: ButtonStyle.Configuration) -> some View {
        configuration.label
            .contentShape(Rectangle())
            .background(configuration.isPressed ? Color(.systemGray).opacity(0.4) : Color.clear)
    }
}

#if DEBUG
struct ContributorView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ContributorView(
                store: .init(
                    initialState: .init(
                        contributors: Contributor.companion.fakes()
                    ),
                    reducer: .empty,
                    environment: ContributorEnvironment(
                        contributorsRepository: FakeContributorsRepository()
                    )
                )
            )
        }
        .preferredColorScheme(.dark)
    }
}
#endif
