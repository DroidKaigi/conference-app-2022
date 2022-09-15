import appioscombined
import UIKit
import SwiftUI

struct ShareTextView: UIViewControllerRepresentable {
    let text: String

    func makeUIViewController(context: UIViewControllerRepresentableContext<ShareTextView>) -> UIActivityViewController {
        return UIActivityViewController(activityItems: [self.text], applicationActivities: nil)
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: UIViewControllerRepresentableContext<ShareTextView>) {}
}

extension TimetableItem {
    var shareText: String {
        return "\(self.title.currentLangTitle)\nhttps://droidkaigi.jp/2022/timetable/\(self.id.value)"
    }
}
