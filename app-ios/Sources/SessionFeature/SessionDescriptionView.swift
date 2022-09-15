import appioscombined
import SwiftUI
import Theme
import Strings

struct SessionDescriptionView: View {
    let text: String

    @State private var isExpanded: Bool = false
    @State private var isTruncated: Bool = false

    private static let lineLimit: Int = 5
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(self.text)
                .font(Font.system(size: 16, weight: .regular, design: .default))
                .lineSpacing(6)
                .lineLimit(self.isExpanded ? nil : SessionDescriptionView.lineLimit)
                .background(
                    Text(self.text)
                        .lineLimit(SessionDescriptionView.lineLimit)
                        .background(
                            GeometryReader { limitedTextGeometry in
                                ZStack {
                                    Text(self.text)
                                        .background(
                                            GeometryReader { fullTextGeometry in
                                                Color.clear.onAppear {
                                                    self.isTruncated = fullTextGeometry.size.height > limitedTextGeometry.size.height
                                                }
                                            }
                                        )
                                }
                                .frame(height: .greatestFiniteMagnitude)
                            }
                        )
                    .hidden()
                )

            if isTruncated {
                Button {
                    withAnimation {
                        self.isTruncated.toggle()
                        self.isExpanded.toggle()
                    }
                } label: {
                    Text(L10n.Session.readMore)
                        .font(Font.system(size: 16, weight: .regular, design: .default))
                        .lineSpacing(6)
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                }
            }
        }
    }
}

#if DEBUG
struct SessionDescriptionView_Previews: PreviewProvider {
    static var previews: some View {
        SessionDescriptionView(
            text: TimetableItem.Session.companion.fake().description_
        )
    }
}
#endif
