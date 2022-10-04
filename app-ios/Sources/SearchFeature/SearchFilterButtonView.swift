import Assets
import SwiftUI
import Theme

enum SearchFilterButtonType {
    case toggle
    case select
}
struct SearchFilterButtonView: View {
    let type: SearchFilterButtonType
    let title: String
    let isFiltered: Bool
    let onTap: () -> Void

    var body: some View {
        Button {
            onTap()
        } label: {
            HStack(spacing: 12) {
                if isFiltered {
                    Image(systemName: "checkmark")
                        .resizable()
                        .frame(width: 12, height: 12)
                }
                Text(title)
                    .font(.system(size: 14, weight: .semibold))
                if type == .select {
                    Image(systemName: "arrowtriangle.down.fill")
                        .resizable()
                        .frame(width: 8, height: 8)
                }
            }
            .foregroundColor(isFiltered ? AssetColors.onSurface.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor)
            .animation(.linear(duration: 0.2), value: isFiltered)
            .padding(.vertical, 8)
            .padding(.horizontal, 16)
            .background(isFiltered ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.background.swiftUIColor
            )
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(isFiltered ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
            .padding(4)
        }
    }
}

#if DEBUG
struct SearchFilterButtonView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            SearchFilterButtonView(
                type: .select,
                title: "Category",
                isFiltered: true,
                onTap: {}
            )
            SearchFilterButtonView(
                type: .select,
                title: "Category",
                isFiltered: false,
                onTap: {}
            )
            SearchFilterButtonView(
                type: .toggle,
                title: "Favorite",
                isFiltered: true,
                onTap: {}
            )
            SearchFilterButtonView(
                type: .toggle,
                title: "Favorite",
                isFiltered: false,
                onTap: {}
            )
        }
    }
}
#endif
