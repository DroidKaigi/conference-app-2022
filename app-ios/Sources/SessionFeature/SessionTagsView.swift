import appioscombined
import SwiftUI
import Theme

struct SessionTagsView: View {
    @State private var totalHeight: CGFloat = 0
    let tags: [Tag]

    public var body: some View {
        VStack {
            self.content(tags: self.tags)
        }
        .frame(height: self.totalHeight)
    }
    private func content(tags: [Tag]) -> some View {
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
            .background(HeightReaderView(height: $totalHeight))
        }
    }
}

#if DEBUG
struct SessionTagsView_Previews: PreviewProvider {
    static var previews: some View {
        SessionTagsView(tags: TimetableItem.Session.companion.fake().tags)
    }
}
#endif

extension TimetableItem {
    var tags: [Tag] {
        return [
            Tag(text: self.room.name.currentLangTitle, color: room.roomColor),
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

private struct HeightPreferenceKey: PreferenceKey {
    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {}
    static var defaultValue: CGFloat = 0
}

private struct HeightReaderView: View {
    @Binding var height: CGFloat

    var body: some View {
        GeometryReader { geometry in
            Color.clear
                .preference(
                    key: HeightPreferenceKey.self,
                    value: geometry.frame(in: .local).size.height
                )
        }
        .onPreferenceChange(HeightPreferenceKey.self) { height in
            self.height = height
        }
    }
}
