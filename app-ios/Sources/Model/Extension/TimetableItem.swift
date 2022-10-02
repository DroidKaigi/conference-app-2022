public extension TimetableItem {
    var minute: Int {
        let startMinute = Int(startsAt.epochSeconds)  / 60
        let endMinute = Int(endsAt.epochSeconds) / 60

        return endMinute - startMinute
    }

    var shareText: String {
        return "\(self.title.currentLangTitle)\nhttps://droidkaigi.jp/2022/timetable/\(self.id.value)"
    }
}
