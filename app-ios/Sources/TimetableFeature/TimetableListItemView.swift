import appioscombined
import SwiftUI
import Assets
import Theme
import Model

struct TimetableListItemView: View {
    let item: TimetableItem
    let isFavorite: Bool
    let minute: Int
    let onFavoriteToggle: @Sendable (TimetableItemId, Bool) -> Void

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            VStack(alignment: .leading) {
                Text(item.title.jaTitle)
                    .multilineTextAlignment(.leading)
                    .font(Font.system(size: 22, weight: .medium, design: .default))
                    .foregroundColor(AssetColors.onBackground.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                if let session = item as? TimetableItem.Session,
                   session.speakers.isEmpty == false {
                    HStack {
                        ForEach(session.speakers, id: \.self) { speaker in
                            PersonLabel(name: speaker.name, iconUrl: speaker.iconUrl)
                        }
                    }
                }
                HStack {
                    CapsuleText(
                        text: item.room.name.jaTitle,
                        foregroundColor: AssetColors.white.swiftUIColor,
                        backgroundColor: item.room.roomColor
                    )
                    CapsuleText(
                        text: "\(minute)min",
                        foregroundColor: AssetColors.onSurface.swiftUIColor,
                        backgroundColor: AssetColors.secondaryContainer.swiftUIColor
                    )
                }
            }
            Button {
                onFavoriteToggle(item.id, isFavorite)
            } label: {
                (isFavorite ? Assets.bookmark.swiftUIImage : Assets.bookmarkBorder.swiftUIImage)
                    .frame(width: 24, height: 24)
            }
        }
    }
}

#if DEBUG
struct TimetableListItemView_Previews: PreviewProvider {
    static var previews: some View {
        let item = TimetableItemWithFavorite.companion.fake()
        TimetableListItemView(
            item: item.timetableItem,
            isFavorite: item.isFavorited,
            minute: calculateMinute(
                startSeconds: Int(item.timetableItem.startsAt.epochSeconds),
                endSeconds: Int(item.timetableItem.endsAt.epochSeconds)
            ),
            onFavoriteToggle: { _, _ in }
        )
    }
}
#endif
