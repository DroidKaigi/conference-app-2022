public enum TimetableItemType: Identifiable, Equatable {
    case general(TimetableItemWithFavorite, Int)
    case spacing(Int)

    public var id: String {
        switch self {
        case let .general(item, _):
            return item.timetableItem.id.value
        case .spacing:
            return UUID().uuidString
        }
    }
}
