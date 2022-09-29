import appioscombined
import Assets
import CommonComponents
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct TimetableState: Equatable {
    public var dayToTimetable: [DroidKaigi2022Day: Timetable]
    public var selectedDay: DroidKaigi2022Day
    public var showDate: Bool
    public var showSheet: Bool
    public var isLoading: Bool

    public init(
        dayToTimetable: [DroidKaigi2022Day: Timetable] = [:],
        selectedDay: DroidKaigi2022Day = .day1,
        showDate: Bool = true,
        showSheet: Bool = true,
        isLoading: Bool = true
    ) {
        self.dayToTimetable = dayToTimetable
        self.selectedDay = selectedDay
        self.showDate = showDate
        self.showSheet = showSheet
        self.isLoading = isLoading
    }
}

public enum TimetableAction {
    case refresh
    case refreshResponse(TaskResult<DroidKaigiSchedule>)
    case selectDay(DroidKaigi2022Day)
    case selectItem(TimetableItemWithFavorite)
    case setFavorite(TimetableItemId, Bool)
    case scroll(CGPoint)
    case search
    case switchContent
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
        state.isLoading = true
        return .run { @MainActor subscriber in
            try await environment.sessionsRepository.refresh()
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
        state.isLoading = false
        return .none
    case .refreshResponse(.failure):
        state.isLoading = false
        return .none
    case let .selectDay(day):
        state.selectedDay = day
        return .init(value: .refresh)
    case let .setFavorite(id, currentIsFavorite):
        return .run { @MainActor _ in
            try await environment.sessionsRepository.setFavorite(sessionId: id, favorite: !currentIsFavorite)
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case let .scroll(position):
        state.showDate = position.y >= TimetableView.scrollThreshold
        return .none
    case .selectItem:
        return .none
    case .search:
        return .none
    case .switchContent:
        state.showSheet.toggle()
        return .none
    }
}

public struct TimetableView: View {
    private let store: Store<TimetableState, TimetableAction>

    public init(store: Store<TimetableState, TimetableAction>) {
        self.store = store
    }

    static let scrollThreshold: CGFloat = 88

    public var body: some View {
        WithViewStore(store) { viewStore in
            NavigationView {
                ZStack(alignment: .top) {

                    if viewStore.state.showSheet {
                        TimetableSheetView(store: store)
                            .scrollThreshold(Self.scrollThreshold)
                            .onScroll {
                                viewStore.send(.scroll($0))
                            }
                    } else {
                        TimetableListView(store: store)
                            .scrollThreshold(Self.scrollThreshold)
                            .onScroll {
                                viewStore.send(.scroll($0))
                            }
                    }
                    if viewStore.state.isLoading {
                        LoadingView()
                    }

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
                                    if viewStore.showDate {
                                        Text("\(startDay)")
                                            .font(Font.system(size: 24, weight: .semibold))
                                            .frame(height: 32)
                                            .transition(.move(edge: .top).combined(with: .opacity))
                                    }
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
                    .animation(.linear(duration: 0.2), value: viewStore.showDate)
                }.animation(Animation.easeInOut(duration: 0.3), value: viewStore.state.showSheet)
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
                                viewStore.send(.search)
                            } label: {
                                Assets.search.swiftUIImage
                                    .renderingMode(.template)
                            }
                            Button {
                                viewStore.send(.switchContent)
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
