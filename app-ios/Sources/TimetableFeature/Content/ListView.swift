import Assets
import CommonComponents
import ComposableArchitecture
import Model
import SwiftUI
import Theme

struct TimetableListView: View, ScrollDetectable {

    var scrollThreshold: CGFloat = 0
    var onScroll: (CGPoint) -> Void = { _ in }

    struct ViewState: Equatable {
        var timeGroupTimetableItems: [TimetableTimeGroupItems]

        init(state: TimetableState) {
            guard let timetable = state.dayToTimetable[state.selectedDay] else {
                self.timeGroupTimetableItems = []
                return
            }
            self.timeGroupTimetableItems = Set(timetable.timetableItems.map { TimetableTimeGroupItems.Duration(startsAt: $0.startsAt, endsAt: $0.endsAt) })
                .map { duration in
                    let items = timetable.contents
                        .filter { itemWithFavorite in
                            itemWithFavorite.timetableItem.startsAt == duration.startsAt && itemWithFavorite.timetableItem.endsAt == duration.endsAt
                        }
                        .sorted {
                            $0.timetableItem.room.sort < $1.timetableItem.room.sort
                        }
                    return TimetableTimeGroupItems(
                        duration: duration,
                        items: items
                    )
                }
                .sorted {
                    $0.startsAt == $1.startsAt ? $0.minute < $1.minute : $0.startsAt < $1.startsAt
                }
        }
    }

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

                LazyVStack(spacing: 32) {
                    ForEach(viewStore.timeGroupTimetableItems) { timetableTimeGroupItems in
                        HStack(alignment: .top, spacing: 28) {
                            VStack(alignment: .center, spacing: 0) {
                                SessionTimeView(
                                    startsAt: timetableTimeGroupItems.startsAt,
                                    endsAt: timetableTimeGroupItems.endsAt
                                )
                            }
                            .foregroundColor(AssetColors.onBackground.swiftUIColor)
                            VStack(spacing: 32) {
                                ForEach(timetableTimeGroupItems.items, id: \.timetableItem.id.value) { timetableItemWithFavorite in
                                    let item = timetableItemWithFavorite.timetableItem
                                    let isFavorite = timetableItemWithFavorite.isFavorited
                                    TimetableListItemView(
                                        item: item,
                                        isFavorite: isFavorite,
                                        onFavoriteToggle: { id, currentIsFavorite in
                                            viewStore.send(.setFavorite(id, currentIsFavorite))
                                        }
                                    )
                                    .onTapGesture {
                                        viewStore.send(.selectItem(timetableItemWithFavorite))
                                    }
                                }
                            }
                        }
                        .padding(.horizontal, 16)
                    }
                }
                .padding(.vertical, 24)
                .background(
                    ScrollDetector(coordinateSpace: .named("TimetableListView"))
                        .onDetect { position in
                            onScroll(position)
                        }
                )
            }
            .coordinateSpace(name: "TimetableListView")
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
