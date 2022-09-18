import SwiftUI
import Theme

struct Tag: Equatable, Hashable {
    let text: String
    let color: Color
}

extension Tag {
    var view: some View {
        return CapsuleText(text: self.text, foregroundColor: AssetColors.white.swiftUIColor, backgroundColor: self.color)
    }
}
