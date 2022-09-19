import SwiftUI
import Kingfisher

public struct NetworkImage: View {

    public let url: URL?
    public let placeholder: (() -> AnyView)?
    public let failure: ((KingfisherError) -> AnyView)?

    // cache settings
    public let enableCache: Bool
    public let cacheInMemory: Bool

    @State private var error: KingfisherError?

    init(
        url: URL?,
        enableCache: Bool = true,
        cacheInMemory: Bool = true,
        placeholder: (() -> AnyView)? = nil,
        failure: ((KingfisherError) -> AnyView)? = nil
    ) {
        self.url = url
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
                        failure:  { error in
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
