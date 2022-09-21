import SwiftUI

public struct LoadingView: View {

    public init() {}

    public var body: some View {
        ZStack {
            Color.black
                .opacity(0.5)
            ProgressView()
        }
    }
}
