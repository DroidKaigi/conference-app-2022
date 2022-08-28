import appioscombined

public enum TimetableItemType: Identifiable, Equatable {
    case general(TimetableItem, Int)
    case spacing(Int)

    public var id: String {
        switch self {
        case let .general(item, _):
            return item.id.value
        case .spacing:
            return UUID().uuidString
        }
    }
}
