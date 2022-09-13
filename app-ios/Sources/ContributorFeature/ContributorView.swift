import SwiftUI
import ComposableArchitecture
import Model

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
        print("\(contributors.count) contributors")
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
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            Text("\(viewStore.contributors.count) Contributors")
                .task {
                    await viewStore.send(.refresh).finish()
                }
        }
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
    }
}
#endif
