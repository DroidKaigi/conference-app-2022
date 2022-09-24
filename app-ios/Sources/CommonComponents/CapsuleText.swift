import SwiftUI

public struct CapsuleText: View {
    let text: String
    let foregroundColor: Color
    let backgroundColor: Color

    public init(
        text: String,
        foregroundColor: Color,
        backgroundColor: Color
    ) {
        self.text = text
        self.foregroundColor = foregroundColor
        self.backgroundColor = backgroundColor
    }

    public var body: some View {
        Text(text)
            .font(Font.system(size: 12, weight: .medium, design: .default))
            .padding(.vertical, 4)
            .padding(.horizontal, 8)
            .foregroundColor(foregroundColor)
            .background(
                Capsule(style: .circular)
                    .foregroundColor(backgroundColor)
            )
    }
}

#if DEBUG
struct CapsuleText_Previews: PreviewProvider {
    static var previews: some View {
        CapsuleText(
            text: "20min",
            foregroundColor: .white,
            backgroundColor: .red
        )
    }
}
#endif
