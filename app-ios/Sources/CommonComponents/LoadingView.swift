import SwiftUI
import Theme

public struct LoadingView: View {

    public init() {}

    public var body: some View {
        ZStack {
            AssetColors.background.swiftUIColor
                .opacity(0.5)
            ProgressView()
                .progressViewStyle(CircularProgressViewStyle(tint: AssetColors.onBackground.swiftUIColor))
        }
    }
}
