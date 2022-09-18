import appioscombined
import Assets
import Model
import SwiftUI
import Theme

public struct TimetableListItemView: View {
    let item: TimetableItem
    let isFavorite: Bool
    let onFavoriteToggle: @Sendable (TimetableItemId, Bool) -> Void

    public init(
        item: TimetableItem,
        isFavorite: Bool,
        onFavoriteToggle: @Sendable @escaping (TimetableItemId, Bool) -> Void
    ) {
        self.item = item
        self.isFavorite = isFavorite
        self.onFavoriteToggle = onFavoriteToggle
    }

    public var body: some View {
        HStack(alignment: .top, spacing: 12) {
            VStack(alignment: .leading, spacing: 8) {
                Text(item.title.jaTitle)
                    .multilineTextAlignment(.leading)
                    .font(Font.system(size: 22, weight: .medium, design: .default))
                    .foregroundColor(AssetColors.onBackground.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                if let session = item as? TimetableItem.Session,
                   session.speakers.isEmpty == false {
                    HStack(spacing: 8) {
                        ForEach(session.speakers, id: \.self) { speaker in
                            PersonLabel(name: speaker.name, iconUrl: speaker.iconUrl)
                        }
                    }
                }
                HStack(spacing: 8) {
                    CapsuleText(
                        text: item.room.name.jaTitle,
                        foregroundColor: AssetColors.white.swiftUIColor,
                        backgroundColor: item.room.roomColor
                    )
                    CapsuleText(
                        text: "\(item.minute)min",
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
        let minute = (Int(item.timetableItem.endsAt.epochSeconds) - Int(item.timetableItem.startsAt.epochSeconds)) / 60
        TimetableListItemView(
            item: item.timetableItem,
            isFavorite: item.isFavorited,
            onFavoriteToggle: { _, _ in }
        )
    }
}
#endif
