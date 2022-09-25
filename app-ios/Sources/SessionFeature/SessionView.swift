import appioscombined
import Assets
import ComposableArchitecture
import Event
import MapFeature
import Model
import SwiftUI
import Theme

public struct SessionState: Equatable {
    public var timetableItemWithFavorite: TimetableItemWithFavorite
    public var isShareSheetShown: Bool = false
    public var mapState: MapState?
    public var eventAddConfirmAlert: AlertState<SessionAction>?

    public init(timetableItemWithFavorite: TimetableItemWithFavorite) {
        self.timetableItemWithFavorite = timetableItemWithFavorite
    }
}

public enum SessionAction: Equatable {
    case tapCalendar
    case tapMap
    case tapFavorite
    case tapShare
    case hideShareSheet
    case showEventAddConfirmAlert
    case hideEventAddConfirmAlert
    case addEvent
    case map(MapAction)
    case hideMap
}

public struct SessionEnvironment {
    public let sessionsRepository: SessionsRepository
    public let eventKitClient: EventKitClientProtocol

    public init(
        sessionsRepository: SessionsRepository,
        eventKitClient: EventKitClientProtocol
    ) {
        self.sessionsRepository = sessionsRepository
        self.eventKitClient = eventKitClient
    }
}

public let sessionReducer = Reducer<SessionState, SessionAction, SessionEnvironment>.combine(
    mapReducer.optional().pullback(
        state: \.mapState,
        action: /SessionAction.map,
        environment: { _ in
            .init()
        }
    ),
    .init { state, action, environment in
        switch action {
        case .tapCalendar:
            let timetableItem = state.timetableItemWithFavorite.timetableItem
            return .run { @MainActor subscriber in
                if try await environment.eventKitClient.requestAccessIfNeeded() {
                    subscriber.send(.showEventAddConfirmAlert)
                }
            }
            .receive(on: DispatchQueue.main.eraseToAnyScheduler())
            .eraseToEffect()
        case .tapMap:
            state.mapState = .init()
            return .none
        case .tapFavorite:
            let timetableItem = state.timetableItemWithFavorite.timetableItem
            let isFavorite = state.timetableItemWithFavorite.isFavorited
            state.timetableItemWithFavorite = TimetableItemWithFavorite(
                timetableItem: timetableItem,
                isFavorited: !isFavorite
            )
            return .run { @MainActor _ in
                try await environment.sessionsRepository.setFavorite(sessionId: timetableItem.id, favorite: !isFavorite)
            }
            .receive(on: DispatchQueue.main.eraseToAnyScheduler())
            .eraseToEffect()
        case .tapShare:
            state.isShareSheetShown = true
            return .none
        case .hideShareSheet:
            state.isShareSheetShown = false
            return .none
        case .showEventAddConfirmAlert:
            state.eventAddConfirmAlert = .init(
                title: .init(StringsKt.shared.session_event_add_confirm.localized()),
                buttons: [
                    .cancel(
                        .init(StringsKt.shared.session_event_add_confirm_cancel.localized()),
                        action: .send(.hideEventAddConfirmAlert)
                    ),
                    .default(
                        .init(StringsKt.shared.session_event_add_confirm_ok.localized()),
                        action: .send(.addEvent)
                    ),
                ]
            )
            return .none
        case .hideEventAddConfirmAlert:
            state.eventAddConfirmAlert = nil
            return .none
        case .addEvent:
            let timetableItem = state.timetableItemWithFavorite.timetableItem
            do {
                try environment.eventKitClient.addEvent(
                    title: timetableItem.title.jaTitle,
                    startDate: timetableItem.startsAt.toDate(),
                    endDate: timetableItem.endsAt.toDate()
                )
            } catch let error {
                print(error)
            }
            return .none
        case .map:
            return .none
        case .hideMap:
            state.mapState = nil
            return .none
        }
    }
)

public struct SessionView: View {
    @Environment(\.presentationMode) var presentationMode

    private let store: Store<SessionState, SessionAction>

    public init(store: Store<SessionState, SessionAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(self.store) { viewStore in
            let timetableItem = viewStore.timetableItemWithFavorite.timetableItem
            let isFavorited = viewStore.timetableItemWithFavorite.isFavorited

            VStack(alignment: .leading) {
                Button {
                    self.presentationMode.wrappedValue.dismiss()
                } label: {
                    Assets.close.swiftUIImage
                }
                .padding(.leading)
                .padding([.top, .bottom], 16)

                ScrollView {
                    VStack(alignment: .leading, spacing: 48) {
                        VStack(alignment: .leading, spacing: 24) {
                            Text(timetableItem.title.currentLangTitle)
                                .frame(maxWidth: .infinity, alignment: .topLeading)
                                .font(Font.system(size: 32, weight: .medium, design: .default))

                            SessionTagsView(tags: timetableItem.tags)

                            VStack(alignment: .leading, spacing: 8) {
                                SessionDurationView(durationString: timetableItem.durationString)

                                if timetableItem.asSession()?.message != nil {
                                    SessionCancelView()
                                }
                            }
                        }

                        if let session = timetableItem.asSession() {
                            SessionDescriptionView(text: session.description_)
                        }
                        SessionAudienceView(targetAudience: timetableItem.targetAudience)

                        if let session = timetableItem.asSession() {
                            SessionSpeakersView(speakers: session.speakers)
                        }

                        SessionAssetsView(asset: timetableItem.asset)
                    }
                    .padding(.horizontal)
                }

                HStack {
                    HStack {
                        Button {
                            viewStore.send(.tapShare)
                        } label: {
                            Assets.share.swiftUIImage
                        }
                        .frame(width: 48, height: 48)

                        Button {
                            viewStore.send(.tapMap)
                        } label: {
                            Assets.map.swiftUIImage
                        }
                        .frame(width: 48, height: 48)

                        Button {
                            viewStore.send(.tapCalendar)
                        } label: {
                            Assets.calendar.swiftUIImage
                        }
                        .frame(width: 48, height: 48)
                    }
                    Spacer()
                    Button {
                        viewStore.send(.tapFavorite)
                    } label: {
                        if isFavorited {
                            Assets.bookmark.swiftUIImage
                        } else {
                            Assets.bookmarkBorder.swiftUIImage
                        }
                    }
                    .frame(width: 48, height: 48)
                }
                .padding()
                .foregroundColor(AssetColors.onSurface.swiftUIColor)
                .background(AssetColors.surface.swiftUIColor)
            }
            .foregroundColor(AssetColors.onBackground.swiftUIColor)
            .background(AssetColors.background.swiftUIColor)
            .sheet(isPresented: viewStore.binding(get: { $0.isShareSheetShown }, send: .hideShareSheet)) {
                ShareTextView(text: timetableItem.shareText)
            }
            .sheet(isPresented: viewStore.binding(get: { $0.mapState != nil }, send: .hideMap)) {
                IfLetStore(
                    store.scope(
                        state: \.mapState,
                        action: SessionAction.map
                    )
                ) { mapStore in
                    MapView(store: mapStore)
                }
            }
            .alert(store.scope(state: \.eventAddConfirmAlert), dismiss: .hideEventAddConfirmAlert)
        }
    }
}

#if DEBUG
struct SessionView_Previews: PreviewProvider {
    static var previews: some View {
        SessionView(
            store: .init(
                initialState: .init(timetableItemWithFavorite: TimetableItemWithFavorite.companion.fake()),
                reducer: .empty,
                environment: SessionEnvironment(
                    sessionsRepository: FakeSessionsRepository(),
                    eventKitClient: EventKitClientMock()
                )
            )
        )
    }
}
#endif
