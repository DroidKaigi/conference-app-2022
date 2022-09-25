import Foundation
import SwiftUI

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

public extension ColorResource {
    func color() -> Color {
        return getColor(userInterfaceStyle: UIKUIUserInterfaceStyle.uiuserinterfacestylelight).color
    }
}

private extension GraphicsColor {
    var color: Color {
        let maxColor = CGFloat(0xFF)
        return Color(
            UIColor(
                red: CGFloat(red) / maxColor,
                green: CGFloat(green) / maxColor,
                blue: CGFloat(blue) / maxColor,
                alpha: CGFloat(alpha) / maxColor
            )
        )
    }
}
