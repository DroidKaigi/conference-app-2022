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
                        .frame(width: 24, height: 24)
                }
            )
            Text(name)
                .font(Font.system(size: 12, weight: .medium))
                .frame(height: 16)
        }
        .foregroundColor(AssetColors.onSurface.swiftUIColor)
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
