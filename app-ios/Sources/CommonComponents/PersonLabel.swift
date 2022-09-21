import SwiftUI
import Theme

struct PersonLabel: View {
    let name: String
    let iconUrl: String

    var body: some View {
        HStack(alignment: .center, spacing: 8) {
            NetworkImage(url: URL(string: iconUrl)) {
                AnyView(Circle())
            }
            .frame(width: 24, height: 24)
            .clipShape(Circle())
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
