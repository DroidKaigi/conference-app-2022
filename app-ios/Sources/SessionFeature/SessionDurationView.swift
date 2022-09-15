import appioscombined
import Assets
import SwiftUI

extension TimetableItem {
    var durationView: some View {
        HStack {
            Assets.clock.swiftUIImage
                .padding(.trailing, 8)
            Text(self.durationString(languageCode: Locale.current.languageCode))
                .font(Font.system(size: 14, weight: .regular, design: .default))
        }
    }
    private func durationString(languageCode: String?) -> String {
        let formatter = DateIntervalFormatter()
        formatter.dateStyle = .short
        formatter.timeStyle = .long

        let startDate = Date(timeIntervalSince1970: TimeInterval(self.startsAt.epochSeconds))
        let endDate = Date(timeIntervalSince1970: TimeInterval(self.endsAt.epochSeconds))
        
        return formatter.string(from: startDate, to: endDate)
    }
}
