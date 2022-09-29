private let dateFormatter: DateFormatter = {
    let formatter = DateFormatter()
    formatter.dateFormat = "yyyy/MM/dd"
    return formatter
}()

extension Kotlinx_datetimeLocalDate {

    public func toDate(calendar: Calendar = .current) -> Date? {
        let components = DateComponents(calendar: calendar, year: Int(year), month: Int(month.ordinal + 1), day: Int(dayOfMonth))
        return components.date
    }

    public func toRelativeDateString(calendar: Calendar = .current) -> String {
        guard let date = toDate(calendar: calendar) else {
            return ""
        }
        if calendar.isDateInToday(date) {
            return StringsKt.shared.announcement_content_header_title_today.localized()
        }
        if calendar.isDateInYesterday(date) {
            return StringsKt.shared.announcement_content_header_title_yesterday.localized()
        }
        return dateFormatter.string(from: date)
    }
}
