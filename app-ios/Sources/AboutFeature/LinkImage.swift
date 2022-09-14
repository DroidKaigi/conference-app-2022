import SafariView
import SwiftUI

public struct LinkImage: View {

    @State var presentSheet = false

    let image: Image
    let url: URL

    public init(image: Image, url: URL) {
        self.image = image
        self.url = url
    }

    public var body: some View {
        Button<Image>(
            action: { presentSheet = true },
            label: { image }
        )
        .sheet(isPresented: $presentSheet) {
            SafariView(url: url)
        }
    }
}
