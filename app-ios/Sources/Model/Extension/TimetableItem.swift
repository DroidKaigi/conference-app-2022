public extension TimetableItem {
    var minute: Int {
        let startMinute = Int(startsAt.epochSeconds)  / 60
        let endMinute = Int(endsAt.epochSeconds) / 60

        return endMinute - startMinute
    }
}
