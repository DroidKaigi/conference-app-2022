import SwiftUI
import Theme

struct AboutNavigationItemView: View {
    let image: Image
    let title: String
    let subTitle: String?

    var body: some View {
        HStack(spacing: 12) {
            image
                .renderingMode(.template)
            VStack(alignment: .leading) {
                Text(title)
                    .font(.system(size: 14, weight: .bold))
                if let subTitle = subTitle {
                    Text(subTitle)
                        .font(.system(size: 12))
                        .multilineTextAlignment(.leading)
                }
            }
            Spacer()
        }
        .padding(16)
        .frame(minHeight: 56)
    }
}
