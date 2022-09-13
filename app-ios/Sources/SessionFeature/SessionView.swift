import Assets
import Theme
import ComposableArchitecture
import Model
import SwiftUI
import Strings

public struct SessionState: Equatable {
    public var timetableItemWithFavorite: TimetableItemWithFavorite

    public init(timetableItemWithFavorite: TimetableItemWithFavorite) {
        self.timetableItemWithFavorite = timetableItemWithFavorite
    }
}

public enum SessionAction {
    case tapFavorite(TimetableItemId, Bool)
    case tapShare(TimetableItem)
}

public struct SessionEnvironment {
    public let sessionsRepository: SessionsRepository

    public init(sessionsRepository: SessionsRepository) {
        self.sessionsRepository = sessionsRepository
    }
}

public let sessionReducer = Reducer<SessionState, SessionAction, SessionEnvironment> { state, action, environment in

    switch action {
    case .tapFavorite(let sessionId, let isFavorite):
        Task {
            try await environment.sessionsRepository.setFavorite(sessionId: sessionId, favorite: !isFavorite)
        }
        return .none
    case .tapShare(let item):
        return .none
    default:
        return .none
    }
}

public struct SessionView: View {
    @Environment(\.presentationMode) var presentationMode
    @Environment(\.openURL) var openURL

    private let store: Store<SessionState, SessionAction>

    public init(store: Store<SessionState, SessionAction>) {
        self.store = store
    }

    public var body: some View {
        WithViewStore(store) { viewStore in
            let timeTableItem = viewStore.timetableItemWithFavorite.timetableItem
            let isFavorited = viewStore.timetableItemWithFavorite.isFavorited

            VStack(alignment: .leading) {
                Button {
                    self.presentationMode.wrappedValue.dismiss()
                } label: {
                    Image(systemName: "xmark")
                }
                .padding()

                ScrollView {
                    VStack(alignment: .leading) {
                        Text(timeTableItem.title.currentLangTitle)
                            .frame(maxWidth: .infinity, alignment: .topLeading)
                            .font(Font.system(size: 36, weight: .medium, design: .default))
                            .padding(.bottom)

                        let tags = [
                            Tag(text: timeTableItem.room.name.currentLangTitle, color: AssetColors.pink.swiftUIColor),
                            Tag(text: "\(timeTableItem.durationInMinutes())min", color: AssetColors.secondaryContainer.swiftUIColor),
                            Tag(text: timeTableItem.category.title.currentLangTitle, color: AssetColors.secondaryContainer.swiftUIColor)
                        ] + timeTableItem.levels.map {
                            Tag(text: $0, color: AssetColors.secondaryContainer.swiftUIColor)
                        }

                        SessionTagsView(tags: tags)
                            .padding(.bottom, 72)
                        HStack {
                            Image(systemName: "clock")
                                .padding(.trailing, 16)
                            Text(timeTableItem.durationString(isJapanese: Locale.current.languageCode == "ja"))
                                .font(Font.system(size: 14, weight: .regular, design: .default))
                        }
                        .padding(.bottom, 24)

                        if let session = timeTableItem.asSession() {
                            SessionDescriptionView(text: session.description_)
                                .padding([.bottom], 24)
                        }
                        Text(L10n.Session.targetAudience)
                            .font(Font.system(size: 16, weight: .medium, design: .default))
                            .padding(.bottom)
                        Text(timeTableItem.targetAudience)
                            .font(Font.system(size: 14, weight: .regular, design: .default))
                            .lineSpacing(4)
                            .padding(.bottom, 24)
                        if let session = timeTableItem.asSession() {
                            SessionSpeakersView(speakers: session.speakers)
                                .padding(.bottom, 24)
                        }
                        Text(L10n.Session.material)
                            .font(Font.system(size: 16, weight: .medium, design: .default))
                            .padding(.bottom)
                        
                        if let videoUrl = timeTableItem.asset.videoUrl {
                            HStack {
                                Image(systemName: "video")
                                    .padding(.trailing, 16)
                                Button {
                                    self.openURL(URL(string: videoUrl)!)
                                } label: {
                                    Text(L10n.Session.movie)
                                        .font(Font.system(size: 14, weight: .regular, design: .default))
                                }
                            }
                            .padding(.bottom)
                        }
                        
                        if let slidesUrl = timeTableItem.asset.slideUrl {
                            HStack {
                                Image(systemName: "photo.on.rectangle")
                                    .padding(.trailing, 16)
                                Button {
                                    self.openURL(URL(string: slidesUrl)!)
                                } label: {
                                    Text(L10n.Session.slides)
                                        .font(Font.system(size: 14, weight: .regular, design: .default))
                                }
                            }
                            .padding(.bottom, 36)
                        }
                    }
                    .padding(.horizontal)
                }

                HStack {
                    HStack {
                        Button {
                            viewStore.send(.tapShare(timeTableItem))
                        } label: {
                            Image(systemName: "square.and.arrow.up")
                        }
                        .frame(width: 48, height: 48)

                        Button {
                            self.presentationMode.wrappedValue.dismiss()
                        } label: {
                            Assets.map.swiftUIImage
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
                        viewStore.send(.tapFavorite(timeTableItem.id, isFavorited))
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
        }
    }
}

#if DEBUG
struct SessionView_Previews: PreviewProvider {
    static var previews: some View {
        SessionView(
            store: .init(
                initialState: .init(
                    timetableItemWithFavorite: TimetableItemWithFavorite(timetableItem: Timetable.companion.fake().timetableItems.first!, isFavorited: false)),
                reducer: .empty,
                environment: SessionEnvironment(sessionsRepository: FakeSessionsRepository())
            )
        )
    }
}
#endif
