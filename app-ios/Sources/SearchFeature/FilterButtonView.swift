import SwiftUI
import Theme

struct FilterButtonView: View {
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
                        .foregroundColor(isFiltered ? AssetColors.onSurface.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor)
                }
                Text(title)
                    .foregroundColor(isFiltered ? AssetColors.onSurface.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor)
                    .font(.system(size: 14, weight: .semibold))
                Image(systemName: "arrowtriangle.down.fill")
                    .resizable()
                    .frame(width: 8, height: 8)
                    .foregroundColor(isFiltered ? AssetColors.onSurface.swiftUIColor : AssetColors.onSurfaceVariant.swiftUIColor)
            }
            .animation(.linear(duration: 0.2), value: isFiltered)
            .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 16))
            .background(isFiltered ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.background.swiftUIColor
            )
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(isFiltered ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
        }
    }
}

#if DEBUG
struct FilterButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            FilterButtonView(title: "Day", isFiltered: false, onTap: {})
            FilterButtonView(title: "Day1", isFiltered: true, onTap: {})
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.surface.swiftUIColor)
    }
}
#endif
