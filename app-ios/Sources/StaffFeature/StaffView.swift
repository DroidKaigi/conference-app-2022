import appioscombined
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct StaffState: Equatable {
    public var staffs: [Staff]

    public init(staffs: [Staff] = []) {
        self.staffs = staffs
    }
}

public enum StaffAction {
    case refresh
    case refreshResponse(TaskResult<[Staff]>)
    case selectStaff(Staff)
}
public struct StaffEnvironment {
    @Environment(\.openURL) var openURL
    public let staffRepository: StaffRepository

    public init(
        staffRepository: StaffRepository
    ) {
        self.staffRepository = staffRepository
    }
}

public let staffReducer = Reducer<StaffState, StaffAction, StaffEnvironment> { state, action, environment in
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            try await environment.staffRepository.refresh()
            for try await result: [Staff] in environment.staffRepository.staff().stream() {
                await subscriber.send(
                    .refreshResponse(
                        TaskResult {
                            result
                        }
                    )
                )
            }
        }
    case .refreshResponse(.success(let staffs)):
        state.staffs = staffs
        return .none
    case .refreshResponse(.failure):
        return .none
    case .selectStaff(let staff):
        if let url = URL(string: staff.profileUrl) {
            environment.openURL(url)
        }
        return .none
    }
}

public struct StaffView: View {
    private let store: Store<StaffState, StaffAction>

    public init(store: Store<StaffState, StaffAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            ScrollView {
                ForEach(viewStore.staffs, id: \.self) { staff in
                    StaffRowView(staff: staff) {
                        viewStore.send(.selectStaff(staff))
                    }
                }
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
        }
        .background(AssetColors.background.swiftUIColor)
        .navigationTitle(StringsKt.shared.about_staff.localized())
        .navigationBarTitleDisplayMode(.inline)
    }
}

#if DEBUG
struct StaffView_Previews: PreviewProvider {
    static var previews: some View {
        StaffView(
            store: .init(
                initialState: .init(
                    staffs: Staff.companion.fakes()
                ),
                reducer: .empty,
                environment: StaffEnvironment(
                    staffRepository: FakeStaffRepository()
                )
            )
        )
    }
}
#endif
