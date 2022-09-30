import appioscombined
import Assets
import Model
import SwiftUI
import Theme

public struct TimetableListItemView: View {
    let item: TimetableItem
    let isFavorite: Bool
    let onTap: @Sendable () -> Void
    let onFavoriteToggle: @Sendable (TimetableItemId, Bool) -> Void
    let searchText: String

    public init(
        item: TimetableItem,
        isFavorite: Bool,
        onTap: @Sendable @escaping () -> Void,
        onFavoriteToggle: @Sendable @escaping (TimetableItemId, Bool) -> Void,
        searchText: String = ""
    ) {
        self.item = item
        self.searchText = searchText
        self.isFavorite = isFavorite
        self.onTap = onTap
        self.onFavoriteToggle = onFavoriteToggle
    }

    public var body: some View {
        Button {
            onTap()
        } label: {
            HStack(alignment: .top, spacing: 12) {
                VStack(alignment: .leading, spacing: 8) {
                    Text(highlightedString(text: item.title.currentLangTitle, searchText: searchText))
                        .multilineTextAlignment(.leading)
                        .font(Font.system(size: 22, weight: .medium, design: .default))
                        .foregroundColor(AssetColors.onBackground.swiftUIColor)
                        .frame(maxWidth: .infinity, alignment: .leading)
                    if let session = item as? TimetableItem.Session,
                       session.speakers.isEmpty == false {
                        VStack(spacing: 0) {
                            HStack(spacing: 8) {
                                ForEach(session.speakers, id: \.self) { speaker in
                                    PersonLabel(name: speaker.name, iconUrl: speaker.iconUrl)
                                }
                            }
                            if let message = session.message {
                                HStack(spacing: 4) {
                                    Assets.error.swiftUIImage
                                    Text(message.currentLangTitle)
                                        .foregroundColor(AssetColors.error.swiftUIColor)
                                        .font(Font.system(size: 12, weight: .regular, design: .default))
                                }
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

    private func highlightedString(text: String, searchText: String) -> AttributedString {
        var attrString = AttributedString(stringLiteral: text)

        if let range = attrString.range(of: searchText, options: .caseInsensitive) {
            attrString[range].backgroundColor = AssetColors.secondaryContainer.swiftUIColor
        }

        return attrString
    }
}

#if DEBUG
struct TimetableListItemView_Previews: PreviewProvider {
    static var previews: some View {
        let item = TimetableItemWithFavorite.companion.fake()
        TimetableListItemView(
            item: item.timetableItem,
            isFavorite: item.isFavorited,
            onTap: {},
            onFavoriteToggle: { _, _ in }
        )
    }
}
#endif
