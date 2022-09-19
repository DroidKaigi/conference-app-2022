import Assets
import Strings
import SwiftUI
import Theme

struct SessionCancellView: View {
    var body: some View {
        HStack(spacing: 6) {
            Assets.error.swiftUIImage
            Text(L10n.Session.cancellMessage)
                .foregroundColor(AssetColors.error.swiftUIColor)
                .font(Font.system(size: 12, weight: .regular, design: .default))
        }
    }
}

struct SessionCancellView_Previews: PreviewProvider {
    static var previews: some View {

        SessionCancellView()
            .previewLayout(.sizeThatFits)
    }
}
