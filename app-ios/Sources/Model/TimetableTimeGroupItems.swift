import appioscombined

public struct TimetableTimeGroupItems: Identifiable, Equatable, Hashable {
    public var id: String {
        items.first?.timetableItem.id.value ?? UUID().uuidString
    }

    public var startsAt: Date
    public var endsAt: Date
    public var minute: Int
    public var items: [TimetableItemWithFavorite]

    public init(startsAt: Date, endsAt: Date, minute: Int, items: [TimetableItemWithFavorite]) {
        self.startsAt = startsAt
        self.endsAt = endsAt
        self.minute = minute
        self.items = items
    }
}
