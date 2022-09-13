import SwiftUI
import Theme

struct PersonLabel: View {
    let name: String
    let iconUrl: String

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            AsyncImage(
                url: URL(string: iconUrl),
                content: { image in
                    image
                        .resizable()
                        .frame(width: 24, height: 24)
                        .clipShape(Circle())
                }, placeholder: {
                    Circle()
                        .foregroundColor(AssetColors.onSurface.swiftUIColor)
                        .frame(width: 24, height: 24)
                }
            )
            Text(name)
                .singleLineFont(size: 12, weight: .medium, lineHeight: 16)
        }
    }
}

#if DEBUG
struct PersonLabel_Previews: PreviewProvider {
    static var previews: some View {
        PersonLabel(
            name: "speaker name",
            iconUrl: "https://placehold.jp/24x24.png"
        )
    }
}
#endif
