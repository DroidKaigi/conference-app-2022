import Foundation

public extension StringResource {
    func localized() -> String {
        var localizedString = NSLocalizedString(resourceId, bundle: bundle, comment: "")
        localizedString.removeAndroidWords()
        return localizedString
    }
}

private extension String {
    mutating func removeAndroidWords() {
        let androidWords = ["Android", "android", "ANDROID"]
        for word in androidWords {
            while let range = range(of: word) {
                removeSubrange(range)
            }
        }
    }
}
