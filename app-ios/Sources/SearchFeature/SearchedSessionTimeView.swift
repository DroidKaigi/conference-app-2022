import SwiftUI

struct SearchedSessionTimeView: View {
    private var startsAt: Date
    private var endsAt: Date

    init(startsAt: Date, endsAt: Date) {
        self.startsAt = startsAt
        self.endsAt = endsAt
    }

    var body: some View {
        VStack {
            Text(startsAt.sessionTimeString())
                .font(Font(UIFont.systemFont(ofSize: 16, weight: .bold)))
                .frame(height: 24)
            Rectangle()
                .frame(width: 1, height: 4)
            Text(endsAt.sessionTimeString())
                .font(Font(UIFont.systemFont(ofSize: 16, weight: .bold)))
                .frame(height: 24)
        }
    }
}

private extension DateFormatter {
    static let sessionTimeFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "ja_JP")
        formatter.timeZone = TimeZone(identifier: "JST")
        formatter.calendar = Calendar(identifier: .gregorian)
        formatter.dateFormat = "HH:mm"
        return formatter
    }()
}

private extension Date {
    func sessionTimeString() -> String {
        DateFormatter.sessionTimeFormatter.string(from: self)
    }
}
