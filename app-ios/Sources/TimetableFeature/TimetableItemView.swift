import appioscombined
import SwiftUI
import Theme

public struct TimetableItemView: View {
    let item: TimetableItemWithFavorite

    public init(item: TimetableItemWithFavorite) {
        self.item = item
    }

    public var body: some View {
        VStack(alignment: .leading) {
            Text(item.timetableItem.title.jaTitle)
                .frame(maxWidth: CGFloat.infinity, alignment: .topLeading)
                .font(Font.system(size: 16, weight: .bold, design: .default))
                .foregroundColor(AssetColors.white.swiftUIColor)
            Spacer()
                .frame(height: 8)
            Text("\((item.timetableItem.endsAt.epochSeconds - item.timetableItem.startsAt.epochSeconds) / 60)min")
                .font(Font.system(size: 12, weight: .semibold, design: .default))
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .foregroundColor(AssetColors.white.swiftUIColor)
                .background(AssetColors.surfaceVariant.swiftUIColor.opacity(0.16))
                .clipShape(Capsule())
        }
        .padding(8)
        .frame(maxHeight: .infinity, alignment: .top)
        .background(item.isFavorited ? item.timetableItem.room.roomColor : item.timetableItem.room.roomColor.opacity(0.2))
        .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
        .overlay(
            RoundedRectangle(cornerRadius: 16, style: .continuous)
                .strokeBorder(item.timetableItem.room.roomColor, lineWidth: 2)
        )
    }
}

#if DEBUG
struct TimetableItemView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableItemView(
            item: TimetableItemWithFavorite.companion.fake()
        )
        .previewLayout(.fixed(width: 200, height: 160))
    }
}
#endif
