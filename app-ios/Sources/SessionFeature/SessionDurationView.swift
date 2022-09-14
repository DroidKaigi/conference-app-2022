import appioscombined
import SwiftUI

extension TimetableItem {
    
    var durationView: some View {
        HStack {
            Image(systemName: "clock")
                .padding(.trailing, 16)
            Text(self.durationString(languageCode: Locale.current.languageCode))
                .font(Font.system(size: 14, weight: .regular, design: .default))
        }
    }
    
    private func durationString(languageCode: String?) -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "HH:mm"

        let isJapanese = languageCode == "ja"

        let startDate = Date(timeIntervalSince1970: TimeInterval(self.startsAt.epochSeconds))
        let endDate = Date(timeIntervalSince1970: TimeInterval(self.endsAt.epochSeconds))

        let startCalendar = Calendar.current.dateComponents([.month, .day], from: startDate)
        
        let month: String
        if let startMonth = startCalendar.month {
            if isJapanese {
                month = "\(startMonth)月"
            } else {
                month = "\(Calendar.current.monthSymbols[startMonth - 1])"
            }
        } else {
            month = ""
        }
        
        let day: String
        if let startDay = startCalendar.day {
            if isJapanese {
                day = "\(startDay)日"
            } else if startDay / 10 == 1 && startDay % 10 == 1 {
                day = "\(startDay)th"
            } else if startDay % 10 == 1 {
                day = "\(startDay)st"
            } else if startDay % 10 == 2 {
                day = "\(startDay)nd"
            } else if startDay % 10 == 3 {
                day = "\(startDay)rd"
            } else {
                day = "\(startDay)th"
            }
        } else {
            day = ""
        }
        
        let startTime = dateFormatter.string(from: startDate)
        let endTime = dateFormatter.string(from: endDate)
    
        return "\(month) \(day) \(startTime)-\(endTime)"
    }
}
