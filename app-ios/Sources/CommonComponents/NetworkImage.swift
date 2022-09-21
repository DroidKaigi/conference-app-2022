import Kingfisher
import SwiftUI

public struct NetworkImage: View {

    let url: URL?
    let contentMode: SwiftUI.ContentMode
    let placeholder: (() -> AnyView)?
    let failure: ((KingfisherError) -> AnyView)?

    // cache settings
    let enableCache: Bool
    let cacheInMemory: Bool

    @State private var error: KingfisherError?

    public init(
        url: URL?,
        contentMode: SwiftUI.ContentMode = .fill,
        enableCache: Bool = true,
        cacheInMemory: Bool = true,
        placeholder: (() -> AnyView)? = nil,
        failure: ((KingfisherError) -> AnyView)? = nil
    ) {
        self.url = url
        self.contentMode = contentMode
        self.enableCache = enableCache
        self.cacheInMemory = cacheInMemory
        self.placeholder = placeholder
        self.failure = failure
    }

    public var body: some View {
        if let error = error, let failure = failure {
            failure(error)
        } else {
            KFImage(url)
                .wrapPlaceholder(placeholder)
                .cacheOriginalImage(enableCache)
                .cacheMemoryOnly(cacheInMemory)
                .resizable()
                .onFailure { error in
                    self.error = error
                }
                .aspectRatio(contentMode: contentMode)
        }
    }
}

private extension KFImage {
    func wrapPlaceholder(_ placeholder: (() -> AnyView)?) -> KFImage {
        if let placeholder = placeholder {
            return self.placeholder(placeholder)
        } else {
            return self
        }
    }
}

#if DEBUG
struct NetworkImage_Previews: PreviewProvider {
    static var previews: some View {
        // DroidKaigi Icon
        let imageURL = URL(string: "https://avatars.githubusercontent.com/u/10727543?s=200&v=4")
        ScrollView {
            LazyVGrid(columns: [.init(), .init()]) {
                ForEach(0..<100, id: \.self) { _ in
                    NetworkImage(url: imageURL)
                        .frame(width: 120, height: 120)
                }
            }
        }
        .previewDisplayName("Success")

        let invalidImageURL = URL(string: "https://")
        ScrollView {
            LazyVGrid(columns: [.init(), .init()]) {
                ForEach(0..<100, id: \.self) { _ in
                    NetworkImage(
                        url: invalidImageURL,
                        failure: { _ in
                            AnyView(Color.red)
                        }
                    )
                    .frame(width: 120, height: 120)
                }
            }
        }
        .previewDisplayName("Fail")
    }
}
#endif
