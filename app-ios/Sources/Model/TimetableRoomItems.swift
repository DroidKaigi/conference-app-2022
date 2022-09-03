import appioscombined

public struct TimetableRoomItems: Equatable {
    public var room: TimetableRoom
    public var items: [TimetableItemType]

    public init(room: TimetableRoom, items: [TimetableItemType]) {
        self.room = room
        self.items = items
    }
}
