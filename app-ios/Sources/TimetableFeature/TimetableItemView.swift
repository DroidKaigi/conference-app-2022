import appioscombined
import SwiftUI
import Theme

public struct TimetableItemView: View {
    let item: TimetableItem

    public init(item: TimetableItem) {
        self.item = item
    }

    public var body: some View {
        VStack(alignment: .leading) {
            Text(item.title.jaTitle)
                .frame(maxWidth: CGFloat.infinity, alignment: .topLeading)
                .font(Font.system(size: 16, weight: .bold, design: .default))
                .foregroundColor(AssetColors.white.swiftUIColor)
            Spacer()
                .frame(height: 8)
            Text("\((item.endsAt.epochSeconds - item.startsAt.epochSeconds) / 60)min")
                .font(Font.system(size: 12, weight: .semibold, design: .default))
                .padding(.horizontal, 8)
                .padding(.vertical, 4)
                .foregroundColor(AssetColors.white.swiftUIColor)
                .background(AssetColors.surfaceVariant.swiftUIColor.opacity(0.16))
                .clipShape(Capsule())
        }
        .padding(8)
        .frame(maxHeight: .infinity, alignment: .top)
        .background(AssetColors.pink.swiftUIColor)
        .cornerRadius(16)
    }
}

#if DEBUG
struct TimetableItemView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableItemView(
            item: Timetable.companion.fake().timetableItems.first!
        )
        .previewLayout(.sizeThatFits)
    }
}
#endif
