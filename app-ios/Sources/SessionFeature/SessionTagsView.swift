import SwiftUI
import appioscombined
import Theme

extension TimetableItem {
    
    var tagsView: some View {
        let tags = self.tags

        return GeometryReader { geometry in
            var width = CGFloat.zero
            var height = CGFloat.zero

            ZStack(alignment: .topLeading) {
                ForEach(tags, id: \.self) { tag in
                    tag.view
                        .padding([.trailing, .bottom], 8)
                        .alignmentGuide(.leading, computeValue: { dimension in
                            if abs(width - dimension.width) > geometry.size.width {
                                width = 0
                                height -= dimension.height
                            }

                            let result = width
                            if tag == tags.last! {
                                width = 0
                            } else {
                                width -= dimension.width
                            }
                            return result
                        })
                        .alignmentGuide(.top, computeValue: { _ in
                            let result = height
                            if tag == tags.last! {
                                height = 0 // last item
                            }
                            return result
                        })
                }
            }
        }
    }

    private var tags: [Tag] {
        return [
            Tag(text: self.room.name.currentLangTitle, color: AssetColors.pink.swiftUIColor),
            Tag(text: "\(self.durationInMinutes())min", color: AssetColors.secondaryContainer.swiftUIColor),
            Tag(text: self.category.title.currentLangTitle, color: AssetColors.secondaryContainer.swiftUIColor)
        ] + self.levels.map {
            Tag(text: $0, color: AssetColors.secondaryContainer.swiftUIColor)
        }
    }
}
