import appioscombined
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct TimetableState: Equatable {
    public var timetable: Timetable
    public var selectedDay: DroidKaigi2022Day

    public init(
        timetable: Timetable = .init(
            timetableItems: [],
            favorites: .init()
        ),
        selectedDay: DroidKaigi2022Day = .day1
    ) {
        self.timetable = timetable
        self.selectedDay = selectedDay
    }
}

public enum TimetableAction {
    case refresh
    case refreshResponse(TaskResult<Timetable>)
    case selectDay(DroidKaigi2022Day)
    case selectItem(TimetableItem)
}

public struct TimetableEnvironment {
    public init() {}
}

public let timetableReducer = Reducer<TimetableState, TimetableAction, TimetableEnvironment> { state, action, _ in
    switch action {
    case .refresh:
        return .task {
            await .refreshResponse(
                TaskResult {
                    Timetable.companion.fake()
                }
            )
        }
    case let .refreshResponse(.success(timetable)):
        state.timetable = timetable
        return .none
    case .refreshResponse(.failure):
        return .none
    case let .selectDay(day):
        state.selectedDay = day
        return .init(value: .refresh)
    case .selectItem:
        return .none
    }
}

public struct TimetableView: View {
    private let store: Store<TimetableState, TimetableAction>

    public init(store: Store<TimetableState, TimetableAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            VStack {
                HStack(spacing: 8) {
                    ForEach(
                        [DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values())
                    ) { day in
                        let startDay = Calendar.current.component(.day, from: day.start.toDate())
                        Button {
                            viewStore.send(.selectDay(day))
                        } label: {
                            VStack(spacing: 0) {
                                Text(day.name)
                                    .font(Font.system(size: 12, weight: .semibold))
                                Text("\(startDay)")
                                    .font(Font.system(size: 24, weight: .semibold))
                                    .frame(height: 32)
                            }
                            .padding(4)
                            .frame(maxWidth: .infinity)
                            .background(
                                viewStore.selectedDay == day
                                ? AssetColors.secondaryContainer.swiftUIColor
                                : AssetColors.surface.swiftUIColor
                            )
                            .clipShape(Capsule())
                        }
                    }
                }
                .padding(.horizontal, 16)
                .padding(.vertical, 16)
                .foregroundColor(AssetColors.onSurface.swiftUIColor)
                .background(AssetColors.surface.swiftUIColor)

                TimetableSheetView(store: store)
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
            .foregroundColor(AssetColors.onBackground.swiftUIColor)
            .background(AssetColors.background.swiftUIColor)
        }
    }
}

#if DEBUG
struct TimetableView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableView(
            store: .init(
                initialState: .init(
                    timetable: Timetable.companion.fake()
                ),
                reducer: .empty,
                environment: TimetableEnvironment()
            )
        )
    }
}
#endif
