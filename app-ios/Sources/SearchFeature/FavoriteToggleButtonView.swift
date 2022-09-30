import appioscombined
import SwiftUI
import Theme

struct FavoriteToggleButtonView: View {
    let isFavorite: Bool
    let onFavoriteToggle: () -> Void
    var body: some View {
        Button {
            onFavoriteToggle()
        } label: {
            HStack(spacing: 12) {
                if isFavorite {
                    Image(systemName: "checkmark")
                        .resizable()
                        .frame(width: 8, height: 8)
                        .foregroundColor(isFavorite ? AssetColors.onSurface.swiftUIColor :
                                         AssetColors.white.swiftUIColor)
                }
                Text(StringsKt.shared.search_filter_favorites.localized())
                    .foregroundColor(isFavorite ? AssetColors.onSurface.swiftUIColor : AssetColors.white.swiftUIColor)
                    .font(.system(size: 14, weight: .semibold))
            }
            .animation(.linear(duration: 0.1), value: isFavorite)
            .padding(EdgeInsets(top: 8, leading: 16, bottom: 8, trailing: 20))
            .background(isFavorite ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.background.swiftUIColor
            )
            .cornerRadius(8)
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(isFavorite ? AssetColors.secondaryContainer.swiftUIColor : AssetColors.white.swiftUIColor, lineWidth: 1)
            )
        }
    }
}

#if DEBUG
struct ToggleButton_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            FavoriteToggleButtonView(
                isFavorite: false,
                onFavoriteToggle: {}
            )
            FavoriteToggleButtonView(
                isFavorite: true,
                onFavoriteToggle: {}
            )
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.surface.swiftUIColor)
    }
}
#endif
