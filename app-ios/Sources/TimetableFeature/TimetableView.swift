import appioscombined
import ComposableArchitecture
import Model
import SwiftUI
import Theme

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
        state.hours = timetable.hours

        state.roomTimetableItems = Set(timetable.timetableItems.map(\.room))
            .map { room -> TimetableRoomItems in

                var items = timetable.timetableItems
                    .filter {
                        $0.room == room && $0.day == state.selectedDay
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
    case let .selectDay(day):
        state.selectedDay = day
        return .init(value: .refresh)
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

                ScrollView(.vertical) {
                    HStack(alignment: .top, spacing: 0) {
                        Spacer()
                            .frame(width: 16)
                        VStack(spacing: 0) {
                            Spacer()
                                .frame(height: 34)
                            ForEach(viewStore.hours, id: \.self) { hour in
                                Text(dateComponentsFormatter.string(from: DateComponents(hour: hour, minute: 0))!)
                                    .font(Font.system(size: 16, weight: .bold, design: .default))
                                    .frame(
                                        height: TimetableView.minuteHeight * 60,
                                        alignment: .top
                                    )
                            }
                        }
                        Spacer()
                            .frame(width: 16)
                        Divider()
                            .foregroundColor(AssetColors.surfaceVariant.swiftUIColor)
                        ScrollView(.horizontal) {
                            HStack(alignment: .top, spacing: 0) {
                                ForEach(viewStore.roomTimetableItems, id: \.room) { timetableRoomItems in
                                    let room = timetableRoomItems.room
                                    let timetableItems = timetableRoomItems.items
                                    VStack(spacing: 0) {
                                        Text(room.name.jaTitle)
                                            .font(Font.system(size: 14, weight: .bold, design: .default))
                                            .padding(.top, 8)
                                            .padding(.bottom, 16)
                                        Divider()
                                            .foregroundColor(AssetColors.surfaceVariant.swiftUIColor)
                                        ForEach(timetableItems) { item in
                                            if case let .general(item, minutes) = item {
                                                TimetableItemView(item: item)
                                                    .frame(height: CGFloat(minutes) * TimetableView.minuteHeight)
                                            } else if case let .spacing(minutes) = item {
                                                Spacer()
                                                    .frame(maxHeight: CGFloat(minutes) * TimetableView.minuteHeight)
                                            }
                                        }
                                    }
                                    .frame(width: 192)
                                    Divider()
                                        .foregroundColor(AssetColors.surfaceVariant.swiftUIColor)
                                }
                            }
                        }
                    }
                }
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
