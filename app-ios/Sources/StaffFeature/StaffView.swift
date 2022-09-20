import ComposableArchitecture
import Model
import Strings
import SwiftUI

public struct StaffState: Equatable {
    public var staffs: [Staff]
    public init(staffs: [Staff] = []) {
        self.staffs = staffs
    }
}

public enum StaffAction {}
public struct StaffEnvironment {
    public init() {}
}
public let staffReducer = Reducer<StaffState, StaffAction, StaffEnvironment> { _, _, _ in
    return .none
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
                    StaffRowView(staff: staff)
                }
            }
        }
        .navigationTitle(L10n.About.staffs)
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
                environment: StaffEnvironment()
            )
        )
    }
}
#endif
