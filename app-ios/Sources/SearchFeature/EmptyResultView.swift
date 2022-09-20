import Assets
import Strings
import SwiftUI

public struct EmptyResultView: View {

    public init() {}

    public var body: some View {
        VStack {
            Assets.search.swiftUIImage
                .resizable()
                .scaledToFit()
                .frame(width: 58.3, height: 58.3)
            Text(L10n.Search.Empty.message)
        }
    }
}
