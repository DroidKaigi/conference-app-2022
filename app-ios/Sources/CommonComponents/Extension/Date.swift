import Foundation

extension DateFormatter {
    static let sessionTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "ja_JP")
        formatter.timeZone = TimeZone(identifier: "JST")
        formatter.calendar = Calendar(identifier: .gregorian)
        formatter.dateFormat = "HH:mm"
        return formatter
    }()
}

extension Date {
    func sessionTimeString() -> String {
        DateFormatter.sessionTimeFormatter.string(from: self)
    }
}
