import SwiftUI

struct SingleLineFontModifier: ViewModifier {
    let font: UIFont
    let lineHeight: CGFloat

    init(fontSize: CGFloat, fontWeight: UIFont.Weight, lineHeight: CGFloat) {
        self.font = UIFont.systemFont(ofSize: fontSize, weight: fontWeight)
        self.lineHeight = lineHeight
    }

    func body(content: Content) -> some View {
        content
            .font(Font(font))
            .lineSpacing(lineHeight - font.lineHeight)
            .padding(.vertical, (lineHeight - font.lineHeight) / 2)
    }
}

extension View {
    /**
     Single Line Font Modifier
     - to set lineHeight for single line text
     - ref: https://stackoverflow.com/questions/61705184/how-to-set-line-height-for-a-single-line-text-in-swiftui
     */
    func singleLineFont(size: CGFloat, weight: UIFont.Weight, lineHeight: CGFloat) -> some View {
        modifier(SingleLineFontModifier(fontSize: size, fontWeight: weight, lineHeight: lineHeight))
    }
}
