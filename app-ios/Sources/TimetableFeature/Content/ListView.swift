import appioscombined
import Assets
import SwiftUI
import Theme
import Model
import ComposableArchitecture

struct TimetableListView: View {
    struct ViewState: Equatable {
        var timeGroupTimetableItems: [TimetableTimeGroupItems]

        init(state: TimetableState) {
            guard let timetable = state.dayToTimetable[state.selectedDay] else {
                self.timeGroupTimetableItems = []
                return
            }
            self.timeGroupTimetableItems = Set(timetable.timetableItems.map { Duration(startsAt: $0.startsAt, endsAt: $0.endsAt) })
                .map { duration in
                    let startsAt = duration.startsAt
                    let endsAt = duration.endsAt
                    let items = timetable.contents
                        .filter { itemWithFavorite in
                            itemWithFavorite.timetableItem.startsAt == startsAt && itemWithFavorite.timetableItem.endsAt == endsAt
                        }
                        .sorted {
                            $0.timetableItem.room.sort < $1.timetableItem.room.sort
                        }
                    let minute = calculateMinute(
                        startSeconds: Int(startsAt.epochSeconds),
                        endSeconds: Int(endsAt.epochSeconds)
                    )
                    return TimetableTimeGroupItems(
                        startsAt: startsAt.toDate(),
                        endsAt: endsAt.toDate(),
                        minute: minute,
                        items: items
                    )
                }
                .sorted {
                    $0.startsAt == $1.startsAt ? $0.minute < $1.minute : $0.startsAt < $1.startsAt
                }
        }
    }

    /// `startsAt` & `endsAt` pair struct conformed to `Hashable`
    private struct Duration: Hashable {
        let startsAt: Kotlinx_datetimeInstant
        let endsAt: Kotlinx_datetimeInstant
    }

    private let dateComponentsFormatter: DateComponentsFormatter = {
        let formatter = DateComponentsFormatter()
        return formatter
    }()

    let store: Store<TimetableState, TimetableAction>

    var body: some View {
        WithViewStore(store.scope(state: ViewState.init)) { viewStore in
            ScrollView(.vertical) {
                LazyVStack(spacing: 32) {
                    ForEach(viewStore.timeGroupTimetableItems, id: \.self) { timetableTimeGroupItems in
                        HStack(alignment: .top, spacing: 28) {
                            VStack(alignment: .center, spacing: 0) {
                                Text(dateComponentsFormatter.string(from: convertToDateComponents(timetableTimeGroupItems.startsAt))!)
                                    .singleLineFont(size: 16, weight: .bold, lineHeight: 24)
                                Rectangle()
                                    .frame(width: 1, height: 4)
                                Text(dateComponentsFormatter.string(from: convertToDateComponents(timetableTimeGroupItems.endsAt))!)
                                    .singleLineFont(size: 16, weight: .bold, lineHeight: 24)
                            }
                            .foregroundColor(AssetColors.onBackground.swiftUIColor)
                            VStack(spacing: 32) {
                                ForEach(timetableTimeGroupItems.items, id: \.timetableItem.id.value) { timetableItemWithFavorite in
                                    let item = timetableItemWithFavorite.timetableItem
                                    let isFavorite = timetableItemWithFavorite.isFavorited
                                    TimetableListItemView(
                                        item: item,
                                        isFavorite: isFavorite,
                                        minute: timetableTimeGroupItems.minute,
                                        onFavoriteToggle: { id, currentIsFavorite in
                                            viewStore.send(.setFavorite(id, currentIsFavorite))
                                        }
                                    )
                                }
                            }
                        }
                        .padding(.horizontal, 16)
                    }
                }
                .padding(.vertical, 16)
            }
        }
    }

    /// convert `Date` to `DateComponents` with hour and minute
    private func convertToDateComponents(_ date: Date) -> DateComponents {
        Calendar.current.dateComponents([.hour, .minute], from: date)
    }
}

#if DEBUG
struct TimetableListView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableListView(
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
