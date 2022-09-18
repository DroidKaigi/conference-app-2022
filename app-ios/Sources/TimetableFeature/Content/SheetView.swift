import ComposableArchitecture
import CoreModel
import SwiftUI
import Theme

struct TimetableSheetView: View, ScrollDetectable {

    var scrollThreshold: CGFloat = 0
    var onScroll: (CGPoint) -> Void = { _ in }

    struct ViewState: Equatable {
        var roomTimetableItems: [TimetableRoomItems]
        var hours: [Int]

        init(state: TimetableState) {
            guard let timetable = state.dayToTimetable[state.selectedDay] else {
                self.roomTimetableItems = []
                self.hours = []
                return
            }
            self.hours = timetable.hours
            self.roomTimetableItems = Set(timetable.timetableItems.map(\.room))
                .map { room in
                    var items = timetable.contents
                        .filter { itemWithFavorite in
                            itemWithFavorite.timetableItem.room == room
                        }
                        .reduce([TimetableItemType]()) { result, item in
                            var result = result
                            let lastItem = result.last
                            if case .general(let lItem, _) = lastItem, lItem.timetableItem.endsAt != item.timetableItem.startsAt {
                                result.append(.spacing(timetableInterval(
                                    firstItem: lItem.timetableItem,
                                    secondItem: item.timetableItem
                                )))
                            }
                            result.append(
                                TimetableItemType.general(
                                    item,
                                    item.timetableItem.minute
                                )
                            )

                            return result
                        }
                    if case let .general(firstItem, _) = items.first {
                        let hour = Calendar.current.component(.hour, from: firstItem.timetableItem.startsAt.toDate())
                        let minute = Calendar.current.component(.minute, from: firstItem.timetableItem.startsAt.toDate())
                        let firstSpacingItem: TimetableItemType = .spacing(minute + max(hour - timetable.hours.first!, 0) * 60)
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
        }
    }

    private static let minuteHeight: CGFloat = 4
    private static let verticalItemSpacing: CGFloat = 3
    private static let timetableStartTime: DateComponents = .init(hour: 10, minute: 0)
    private let dateComponentsFormatter: DateComponentsFormatter = {
        let formatter = DateComponentsFormatter()
        return formatter
    }()

    let store: Store<TimetableState, TimetableAction>

    var body: some View {
        WithViewStore(store.scope(state: ViewState.init)) { viewStore in
            ScrollView(.vertical) {

                Spacer()
                    .frame(height: scrollThreshold)

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
                                    height: TimetableSheetView.minuteHeight * 60,
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
                                                .frame(height: CGFloat(minutes) * TimetableSheetView.minuteHeight - TimetableSheetView.verticalItemSpacing)
                                                .padding(.bottom, TimetableSheetView.verticalItemSpacing)
                                        } else if case let .spacing(minutes) = item {
                                            Spacer()
                                                .frame(maxHeight: CGFloat(minutes) * TimetableSheetView.minuteHeight)
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
                .background(
                    ScrollDetector(coordinateSpace: .named("TimetableSheetView"))
                        .onDetect { position in
                            onScroll(position)
                        }
                )
            }
            .coordinateSpace(name: "TimetableSheetView")
        }
    }
}

#if DEBUG
struct TimetableSheetView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableSheetView(
            store: .init(
                initialState: .init(
                    dayToTimetable: DroidKaigiSchedule.companion.fake().dayToTimetable
                ),
                reducer: .empty,
                environment: TimetableEnvironment(
                    sessionsRepository: FakeSessionsRepository()
                )
            )
        )
    }
}
#endif
