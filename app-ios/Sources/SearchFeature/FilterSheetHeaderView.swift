import Assets
import SwiftUI
import Theme

struct FilterSheetHeaderView: View {
    let title: String
    let onClose: () -> Void
    var body: some View {
        HStack(spacing: 16) {
            Button {
                onClose()
            } label: {
                Assets.close.swiftUIImage
            }
            Text(title)
                .font(.system(size: 22, weight: .medium))
                .foregroundColor(AssetColors.onSurfaceVariant.swiftUIColor)
            Spacer()
        }
    }
}

struct FilterSheetHeaderView_Previews: PreviewProvider {
    static var previews: some View {
        FilterSheetHeaderView(title: "title") {}
            .background(AssetColors.surface.swiftUIColor)
            .padding(.horizontal, 24)
    }
}
