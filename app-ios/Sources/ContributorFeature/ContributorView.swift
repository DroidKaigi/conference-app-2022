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
            NavigationView {
                List(viewStore.contributors, id: \.id) { contributor in
                    Button(action: {
                        guard let profileUrl = contributor.profileUrl else { return }
                        guard let url = URL(string: profileUrl) else { return }
                        if UIApplication.shared.canOpenURL(url) {
                            UIApplication.shared.open(url)
                        }
                    }, label: {
                        ContributorItemView(contributor: contributor)
                    })
                    .hideListRowSeparator()
                }
                .listStyle(PlainListStyle())
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
        }
    }
}

struct ContributorItemView: View {

    let contributor: Contributor

    var body: some View {
        HStack(spacing: 16) {
            Assets.colorfulLogo.swiftUIImage
                .frame(width: 60, height: 60)
            Text(contributor.username)
                .font(Font.system(size: 16, weight: .bold, design: .default))
                .foregroundColor(AssetColors.onBackground.swiftUIColor)
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
struct ContributorViewItem_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ContributorItemView(contributor: Contributor.companion.fakes().first!)
        }
        .previewLayout(.fixed(width: 390, height: 60 + 8))
        .preferredColorScheme(.dark)
    }
}
#endif
