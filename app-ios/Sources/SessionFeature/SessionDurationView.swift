import appioscombined
import Assets
import SwiftUI

struct SessionDurationView: View {

    let durationString: String

    var body: some View {
        HStack {
            Assets.clock.swiftUIImage
                .padding(.trailing, 8)
            Text(self.durationString)
                .font(Font.system(size: 12, weight: .regular, design: .default))
        }
    }
}

#if DEBUG
struct SessionDurationView_Previews: PreviewProvider {
    static var previews: some View {
        SessionDurationView(durationString: TimetableItem.Session.companion.fake().durationString)
    }
}
#endif

extension TimetableItem {

    var durationString: String {
        let formatter = DateIntervalFormatter()
        formatter.dateStyle = .short
        formatter.timeStyle = .short

        let startDate = Date(timeIntervalSince1970: TimeInterval(self.startsAt.epochSeconds))
        let endDate = Date(timeIntervalSince1970: TimeInterval(self.endsAt.epochSeconds))

        return formatter.string(from: startDate, to: endDate)
    }
}
