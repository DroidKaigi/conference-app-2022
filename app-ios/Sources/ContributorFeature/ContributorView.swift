import ComposableArchitecture
import SwiftUI
import Model
import Assets
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
                    guard let url = URL(string: profileUrl) else { return }
                    if UIApplication.shared.canOpenURL(url) {
                        UIApplication.shared.open(url)
                    }
                })
                .listRowInsets(EdgeInsets())
                .listRowBackground(AssetColors.background.swiftUIColor)
                .hideListRowSeparator()
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
            .listStyle(PlainListStyle())
        }
    }
}

struct ContributorItemView: View {

    let contributor: Contributor
    var action: () -> Void

    var body: some View {
        Button {
            action()
        } label: {
            HStack(spacing: 16) {
                AsyncImage(url: URL(string: contributor.iconUrl)) { image in
                    image.resizable()
                } placeholder: {
                    Color(.systemGray4)
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
        }
    }
}

struct HideListRowSeparatorModifier: ViewModifier {
    func body(content: Content) -> some View {
        if #available(iOS 15.0, *) {
            content.listRowSeparator(.hidden)
        } else {
            content
        }
    }
}

extension View {
    func hideListRowSeparator() -> some View {
        self.modifier(HideListRowSeparatorModifier())
    }
}

#if DEBUG
struct ContributorView_Previews: PreviewProvider {
    static var previews: some View {
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
        .preferredColorScheme(.dark)
    }
}
#endif
