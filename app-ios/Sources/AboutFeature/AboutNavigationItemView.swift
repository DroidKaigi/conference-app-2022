import SwiftUI

struct AboutNavigationItemView: View {
    let image: Image
    let title: String

    var body: some View {
        HStack(spacing: 12) {
            image
                .renderingMode(.template)
            Text(title)
            Spacer()
        }
        .padding(16)
        .frame(minHeight: 56)
    }
}
