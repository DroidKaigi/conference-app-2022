import appioscombined
import SwiftUI
import Theme

extension TimetableItem {
    var tagsView: some View {
        return LazyVGrid(columns: [GridItem(.adaptive(minimum: 100))]) {
            ForEach(self.tags, id: \.self) { tag in
                tag.view
            }
        }
    }

    private var tags: [Tag] {
        return [
            Tag(text: self.room.name.currentLangTitle, color: AssetColors.pink.swiftUIColor),
            Tag(text: "\(self.durationInMinutes)min", color: AssetColors.secondaryContainer.swiftUIColor),
            Tag(text: self.category.title.currentLangTitle, color: AssetColors.secondaryContainer.swiftUIColor)
        ] + self.levels.map {
            Tag(text: $0, color: AssetColors.secondaryContainer.swiftUIColor)
        }
    }
    private var durationInMinutes: Int64 {
        let durationInSeconds = self.endsAt.epochSeconds - self.startsAt.epochSeconds
        return durationInSeconds/60
    }
}
