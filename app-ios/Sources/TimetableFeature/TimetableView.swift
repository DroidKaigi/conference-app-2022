import appioscombined
import ComposableArchitecture
import Model
import SwiftUI

public struct TimetableState: Equatable {
    public var roomTimetableItems: [TimetableRoomItems]
    public var hours: [Int]
    public var selectedDay: DroidKaigi2022Day

    public init(
        roomTimetableItems: [TimetableRoomItems] = [],
        hours: [Int] = [],
        selectedDay: DroidKaigi2022Day = .day1
    ) {
        self.roomTimetableItems = roomTimetableItems
        self.hours = hours
        self.selectedDay = selectedDay
    }
}

public enum TimetableAction {
    case refresh
    case refreshResponse(TaskResult<Timetable>)
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
        state.hours = timetable.hours

        state.roomTimetableItems = Set(timetable.timetableItems.map(\.room))
            .map { room -> TimetableRoomItems in

                var items = timetable.timetableItems
                    .filter {
                        $0.room == room
                    }
                    .reduce([TimetableItemType]()) { result, item in
                        var result = result
                        let lastItem = result.last
                        if case .general(let lItem, _) = lastItem, lItem.endsAt != item.startsAt {
                            result.append(.spacing(calculateMinute(
                                startSeconds: Int(lItem.endsAt.epochSeconds),
                                endSeconds: Int(item.startsAt.epochSeconds)
                            )))
                        }
                        let minute = calculateMinute(
                            startSeconds: Int(item.startsAt.epochSeconds),
                            endSeconds: Int(item.endsAt.epochSeconds)
                        )
                        result.append(
                            TimetableItemType.general(
                                item,
                                minute
                            )
                        )

                        return result
                    }
                if case let .general(firstItem, _) = items.first {
                    let hour = Calendar.current.component(.hour, from: firstItem.startsAt.toDate())
                    let minute = Calendar.current.component(.minute, from: firstItem.startsAt.toDate())
                    let firstSpacingItem: TimetableItemType = .spacing(minute + max(hour - state.hours.first!, 0) * 60)
                    items.insert(firstSpacingItem, at: 0)
                }
                return TimetableRoomItems(
                    room: room,
                    items: items
                )
            }
            .sorted {
                $0.room.sort < $1.room.sort
            }

        return .none
    case .refreshResponse(.failure):
        return .none
    case .selectItem:
        return .none
    }
}

public struct TimetableView: View {
    private static let minuteHeight: CGFloat = 4
    private static let timetableStartTime: DateComponents = .init(hour: 10, minute: 0)

    private let dateComponentsFormatter: DateComponentsFormatter = {
        let formatter = DateComponentsFormatter()
        return formatter
    }()

    private let store: Store<TimetableState, TimetableAction>
    @State var selectedDay: DroidKaigi2022Day = DroidKaigi2022Day.day1

    public init(store: Store<TimetableState, TimetableAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            VStack {
                Picker(selection: $selectedDay) {
                    ForEach(
                        [DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values())
                    ) { day in
                        Text(day.name).tag(day)
                    }
                } label: {
                    Text("Day select")
                }
                .pickerStyle(.segmented)

                ScrollView(.vertical) {
                    HStack(alignment: .top) {
                        VStack(spacing: 0) {
                            Text("Room")
                            ForEach(viewStore.hours, id: \.self) { hour in
                                Text(dateComponentsFormatter.string(from: DateComponents(hour: hour, minute: 0))!)
                                    .frame(
                                        height: TimetableView.minuteHeight * 60,
                                        alignment: .top
                                    )
                            }
                        }
                        ScrollView(.horizontal) {
                            HStack(alignment: .top) {
                                ForEach(viewStore.roomTimetableItems, id: \.room) { timetableRoomItems in
                                    let room = timetableRoomItems.room
                                    let timetableItems = timetableRoomItems.items
                                    VStack(spacing: 0) {
                                        Text(room.name.jaTitle)
                                        ForEach(timetableItems) { item in
                                            if case let .general(item, minutes) = item {
                                                TimetableItemView(item: item)
                                                    .frame(height: CGFloat(minutes) * TimetableView.minuteHeight)
                                                    .background(randomColor())
                                            } else if case let .spacing(minutes) = item {
                                                Spacer()
                                                    .frame(maxHeight: CGFloat(minutes) * TimetableView.minuteHeight)
                                            }
                                        }
                                    }
                                    .frame(width: 200)
                                }
                            }
                        }
                    }
                }
            }
            .task {
                await viewStore.send(.refresh).finish()
            }
        }
    }
}

#if DEBUG
struct TimetableView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableView(
            store: .init(
                initialState: .init(
                    roomTimetableItems: [],
                    hours: [10, 11, 12, 13],
                    selectedDay: .day1
                ),
                reducer: .empty,
                environment: TimetableEnvironment()
            )
        )
    }
}
#endif

func randomColor() -> Color {
    let red = Double.random(in: 0...1)
    let green = Double.random(in: 0...1)
    let blue = Double.random(in: 0...1)
    return Color(red: red, green: green, blue: blue)
}
