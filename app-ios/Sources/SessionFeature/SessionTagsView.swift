import SwiftUI
import appioscombined
import Theme

struct SessionTagsView: View {
    let tags: [Tag]
    
    public var body: some View {
        GeometryReader { geometry in
            var width = CGFloat.zero
            var height = CGFloat.zero

            ZStack(alignment: .topLeading) {
                ForEach(self.tags, id: \.self) { tag in
                    tag.view()
                        .padding([.trailing, .bottom], 8)
                        .alignmentGuide(.leading, computeValue: { dimension in
                            if (abs(width - dimension.width) > geometry.size.width) {
                                width = 0
                                height -= dimension.height
                            }

                            let result = width
                            if tag == self.tags.last! {
                                width = 0 //last item
                            } else {
                                width -= dimension.width
                            }
                            return result
                        })
                        .alignmentGuide(.top, computeValue: { _ in
                            let result = height
                            if tag == self.tags.last! {
                                height = 0 // last item
                            }
                            return result
                        })
                }
            }
        }
    }
}
