import SwiftUI
import SafariServices

public struct SafariView: UIViewControllerRepresentable {

    private let url: URL
    private let config: SFSafariViewController.Configuration

    public init(url: URL, config: SFSafariViewController.Configuration = .init()) {
        self.url = url
        self.config = config
    }

    public func makeUIViewController(context: Context) -> SFSafariViewController {
        let safariViewController = SFSafariViewController(url: url, configuration: config)
        return safariViewController
    }

    public func updateUIViewController(_ uiViewController: SFSafariViewController, context: Context) {}
}
