import SwiftUI
import Assets
import Theme
import Model

struct TimetableListItemView: View {
    let item: TimetableItem
    let isFavorite: Bool
    let minute: Int

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            VStack(alignment: .leading) {
                Text(item.title.jaTitle)
                    .multilineTextAlignment(.leading)
                    .font(Font.system(size: 22, weight: .medium, design: .default))
                    .foregroundColor(AssetColors.onBackground.swiftUIColor)
                    .frame(maxWidth: .infinity, alignment: .leading)
                HStack {
                    Text(item.room.name.jaTitle)
                        .padding(.vertical, 4)
                        .padding(.horizontal, 8)
                        .foregroundColor(AssetColors.white.swiftUIColor)
                        .background(
                            Capsule(style: .circular)
                                .foregroundColor(item.room.roomColor)
                        )
                    Text("\(minute)min")
                        .padding(.vertical, 4)
                        .padding(.horizontal, 8)
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)
                        .background(
                            Capsule(style: .circular)
                                .foregroundColor(AssetColors.secondaryContainer.swiftUIColor)
                        )
                }
                .font(Font.system(size: 12, weight: .medium, design: .default))
            }
            Button {
                // TODO: setFavorite
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
            )
        )
    }
}
#endif
