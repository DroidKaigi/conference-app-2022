import appioscombined
import Assets
import ComposableArchitecture
import CoreModel
import Strings
import SwiftUI
import Theme

public struct SessionState: Equatable {
    public var timetableItemWithFavorite: TimetableItemWithFavorite
    public var isShareSheetShown: Bool = false

    public init(timetableItemWithFavorite: TimetableItemWithFavorite) {
        self.timetableItemWithFavorite = timetableItemWithFavorite
    }
}

public enum SessionAction {
    case tapCalendar(TimetableItem)
    case tapFavorite(TimetableItemId, Bool)
    case tapShare
    case hideShareSheet
}

public struct SessionEnvironment {
    public let sessionsRepository: SessionsRepository

    public init(sessionsRepository: SessionsRepository) {
        self.sessionsRepository = sessionsRepository
    }
}

public let sessionReducer = Reducer<SessionState, SessionAction, SessionEnvironment> { state, action, environment in

    switch action {
    case .tapCalendar(let item):
        return .run { @MainActor _ in
            // TODO: Add event using EventKit
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case .tapFavorite(let sessionId, let isFavorite):
        state.timetableItemWithFavorite = TimetableItemWithFavorite(
            timetableItem: state.timetableItemWithFavorite.timetableItem,
            isFavorited: !isFavorite
        )
        return .run { @MainActor _ in
            try await environment.sessionsRepository.setFavorite(sessionId: sessionId, favorite: !isFavorite)
        }
        .receive(on: DispatchQueue.main.eraseToAnyScheduler())
        .eraseToEffect()
    case .tapShare:
        state.isShareSheetShown = true
        return .none
    case .hideShareSheet:
        state.isShareSheetShown = false
        return .none
    }
}

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

                            SessionDurationView(durationString: timetableItem.durationString)
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
                            self.presentationMode.wrappedValue.dismiss()
                        } label: {
                            Assets.navigate.swiftUIImage
                        }
                        .frame(width: 48, height: 48)

                        Button {
                            self.presentationMode.wrappedValue.dismiss()
                        } label: {
                            Assets.calendar.swiftUIImage
                        }
                        .frame(width: 48, height: 48)
                    }
                    Spacer()
                    Button {
                        viewStore.send(.tapFavorite(timetableItem.id, isFavorited))
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
                environment: SessionEnvironment(sessionsRepository: FakeSessionsRepository())
            )
        )
    }
}
#endif
