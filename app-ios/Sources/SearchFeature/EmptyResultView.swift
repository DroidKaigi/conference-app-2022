import appioscombined
import Assets
import SwiftUI

public struct EmptyResultView: View {

    public init() {}

    public var body: some View {
        VStack {
            Assets.search.swiftUIImage
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
            Text(StringsKt.shared.search_empty_result.localized())
        }
    }
}
