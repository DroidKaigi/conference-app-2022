import SwiftUI

struct Tag: Equatable, Hashable {
    let text: String
    let color: Color
}

extension Tag {
    var view: some View {
        return Text(self.text)
            .font(Font.system(size: 14, weight: .regular, design: .default))
            .padding(.horizontal)
            .padding(.vertical, 4)
            .background(
                Capsule()
                    .fill(self.color)
            )
    }
}
