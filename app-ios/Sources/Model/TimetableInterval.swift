import appioscombined

public func timetableInterval(firstItem: TimetableItem, secondItem: TimetableItem) -> Int {
    let firstEndSeconds = Int(firstItem.endsAt.epochSeconds)
    let secondStartSeconds = Int(secondItem.startsAt.epochSeconds)

    return (secondStartSeconds - firstEndSeconds) / 60
}
