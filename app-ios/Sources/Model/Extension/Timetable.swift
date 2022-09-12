import appioscombined

extension Timetable {
    public var hours: [Int] {
        let hours = timetableItems.flatMap { item in
            return [item.startsAt.epochSeconds, item.endsAt.epochSeconds]
        }
        .map(Int.init)
        .map(TimeInterval.init)
        .map(Date.init(timeIntervalSince1970:))
        .map { Calendar.current.component(.hour, from: $0) }

        let maxHour = hours.max() ?? 0
        let minHour = hours.min() ?? 0

        return Array(minHour...maxHour)
    }
}
