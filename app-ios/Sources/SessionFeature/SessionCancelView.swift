import appioscombined
import Assets
import SwiftUI
import Theme

struct SessionCancelView: View {
    var body: some View {
        HStack(spacing: 6) {
            Assets.error.swiftUIImage
            Text(StringsKt.shared.session_cancel.localized())
                .foregroundColor(AssetColors.error.swiftUIColor)
                .font(Font.system(size: 12, weight: .regular, design: .default))
        }
    }
}

struct SessionCancellView_Previews: PreviewProvider {
    static var previews: some View {

        SessionCancelView()
            .previewLayout(.sizeThatFits)
    }
}
