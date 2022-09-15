import UIKit
import SwiftUI

struct ShareTextView: UIViewControllerRepresentable {
    let text: String

    func makeUIViewController(context: UIViewControllerRepresentableContext<ShareTextView>) -> UIActivityViewController {
        return UIActivityViewController(activityItems: [self.text], applicationActivities: nil)
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: UIViewControllerRepresentableContext<ShareTextView>) {}
}
