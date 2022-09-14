import SwiftUI
import ComposableArchitecture
import Model
import Assets
import Theme
import NukeUI

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
            ScrollView {
                LazyVStack(spacing: 0) {
                    ForEach(viewStore.contributors, id: \.id) { contributor in
                        Button(action: {
                            guard let profileUrl = contributor.profileUrl else { return }
                            guard let url = URL(string: profileUrl) else { return }
                            if UIApplication.shared.canOpenURL(url) {
                                UIApplication.shared.open(url)
                            }
                        }, label: {
                            ContributorItemView(contributor: contributor)
                        })
                    }
                }
                .task {
                    await viewStore.send(.refresh).finish()
                }
            }
            .background(AssetColors.background.swiftUIColor)
        }
    }
}

struct ContributorItemView: View {

    let contributor: Contributor

    var body: some View {
        HStack(alignment: .center, spacing: 16) {
            LazyImage(url: URL(string: contributor.iconUrl))
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
