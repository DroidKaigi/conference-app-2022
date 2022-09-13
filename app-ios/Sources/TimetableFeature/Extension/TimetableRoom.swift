import appioscombined
import SwiftUI
import Theme

extension TimetableRoom {
    var roomColor: Color {
        switch name.enTitle {
        case "App Bar": return AssetColors.pink.swiftUIColor
        case "Backdrop": return AssetColors.orange.swiftUIColor
        case "Cards": return AssetColors.blue.swiftUIColor
        case "Dialogs": return AssetColors.purple.swiftUIColor
        case "Online": return AssetColors.purple.swiftUIColor
        default: return AssetColors.pink.swiftUIColor
        }
    }
}
