import appioscombined
import Assets
import ComposableArchitecture
import Model
import SwiftUI
import Theme

public struct TimetableState: Equatable {
    public var dayToTimetable: [DroidKaigi2022Day: Timetable]
    public var selectedDay: DroidKaigi2022Day
    public var showTabDay: Bool

    public init(
        dayToTimetable: [DroidKaigi2022Day: Timetable] = [:],
        selectedDay: DroidKaigi2022Day = .day1,
        showTabDay: Bool = true
    ) {
        self.dayToTimetable = dayToTimetable
        self.selectedDay = selectedDay
        self.showTabDay = showTabDay
    }
}

public enum TimetableAction {
    case refresh
    case refreshResponse(TaskResult<DroidKaigiSchedule>)
    case selectDay(DroidKaigi2022Day)
    case selectItem(TimetableItem)
    case setFavorite(TimetableItemId, Bool)
    case scroll(CGPoint)
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
    case let .setFavorite(id, currentIsFavorite):
        return .run { @MainActor _ in
            try await environment.sessionsRepository.setFavorite(sessionId: id, favorite: !currentIsFavorite)
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case let .scroll(position):
        state.showTabDay = position.y >= 0
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
                ZStack(alignment: .top) {

                    TimetableSheetView(store: store)
                        .onScroll {
                            viewStore.send(.scroll($0))
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
                                    if viewStore.showTabDay {
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
                                .animation(.linear(duration: 0.2), value: viewStore.showTabDay)
                            }
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 16)
                    .foregroundColor(AssetColors.onSurface.swiftUIColor)
                    .background(AssetColors.surface.swiftUIColor)
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

struct ScrollDetector: View {

    private struct ScrollPreferenceKey: PreferenceKey {
        static var defaultValue: CGRect = .zero
        static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
            value = nextValue()
        }
    }

    let coordinateSpace: CoordinateSpace
    var onDetect: (CGPoint) -> Void

    init(coordinateSpace: CoordinateSpace, onDetect: @escaping (CGPoint) -> Void = { _ in }) {
        self.coordinateSpace = coordinateSpace
        self.onDetect = onDetect
    }

    var body: some View {
        GeometryReader { proxy in
            VStack {
            }
            .preference(key: ScrollPreferenceKey.self, value: proxy.frame(in: coordinateSpace))
        }
        .onPreferenceChange(ScrollPreferenceKey.self) {
            onDetect($0.origin)
        }
    }

    func onDetect(_ action: @escaping (CGPoint) -> Void) -> some View {
        var view = self
        view.onDetect = action
        return view
    }
}

protocol ScrollDetectable {
    var onScroll: (CGPoint) -> Void { get set }
}
extension ScrollDetectable where Self: View {
    func onScroll(perform action: @escaping (CGPoint) -> Void) -> some View {
        var view = self
        view.onScroll = action
        return view
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
