import appioscombined
import SwiftUI
import Theme

public extension TimetableRoom {
    var roomColor: Color {
        switch name.enTitle {
        case "App Bar": return AssetColors.pink.swiftUIColor
        case "Backdrop": return AssetColors.orange.swiftUIColor
        case "Cards": return AssetColors.yellow.swiftUIColor
        case "Dialogs": return AssetColors.blue.swiftUIColor
        case "Online": return AssetColors.purple.swiftUIColor
        default: return AssetColors.pink.swiftUIColor
        }
    }
}
