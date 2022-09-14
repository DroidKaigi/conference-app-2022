import appioscombined
import Assets
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct TimetableState: Equatable {
    public var dayToTimetable: [DroidKaigi2022Day: Timetable]
    public var selectedDay: DroidKaigi2022Day

    public init(
        dayToTimetable: [DroidKaigi2022Day: Timetable] = [:],
        selectedDay: DroidKaigi2022Day = .day1
    ) {
        self.dayToTimetable = dayToTimetable
        self.selectedDay = selectedDay
    }
}

public enum TimetableAction {
    case refresh
    case refreshResponse(TaskResult<DroidKaigiSchedule>)
    case selectDay(DroidKaigi2022Day)
    case selectItem(TimetableItem)
}

public struct TimetableEnvironment {
    public let sessionsRepository: SessionsRepository

    public init(sessionsRepository: SessionsRepository) {
        self.sessionsRepository = sessionsRepository
    }
}

public let timetableReducer = Reducer<TimetableState, TimetableAction, TimetableEnvironment> { state, action, environment in
    switch action {
    case .refresh:
        return .run { @MainActor subscriber in
            for try await result: DroidKaigiSchedule in environment.sessionsRepository.droidKaigiScheduleFlow().stream() {
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
    case let .refreshResponse(.success(droidKaigiSchedule)):
        state.dayToTimetable = droidKaigiSchedule.dayToTimetable
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
            NavigationView {
                VStack(spacing: 0) {
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
                .toolbar {
                    ToolbarItemGroup(placement: .navigationBarLeading) {
                        Assets.colorfulLogo.swiftUIImage
                    }
                    ToolbarItemGroup(placement: .navigationBarTrailing) {
                        Group {
                            Button {
                                // TODO: search
                            } label: {
                                Assets.search.swiftUIImage
                                    .renderingMode(.template)
                            }
                            Button {
                                // TODO: switch TimetableView
                            } label: {
                                Assets.calendar.swiftUIImage
                                    .renderingMode(.template)
                            }
                        }
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)
                    }

                }
                .navigationBarTitleDisplayMode(.inline)
            }
            .navigationViewStyle(.stack)
        }
    }
}

#if DEBUG
struct TimetableView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableView(
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
