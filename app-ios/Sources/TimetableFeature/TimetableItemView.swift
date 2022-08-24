import appioscombined
import SwiftUI

public struct TimetableItemView: View {
    let item: TimetableItem

    public init(item: TimetableItem) {
        self.item = item
    }

    public var body: some View {
        VStack(alignment: .leading) {
            Text(item.title.jaTitle)
            Spacer()
            Text("\(item.startsAt.toDate().formatted(date: .omitted, time: .shortened)) - \(item.endsAt.toDate().formatted(date: .omitted, time: .shortened))")
            if let session = item as? TimetableItem.Session {
                Text(session.speakers.first!.name)
            }
        }
        .padding()
    }
}

#if DEBUG
struct TimetableItemView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableItemView(
            item: Timetable.companion.fake().timetableItems.first!
        )
    }
}
#endif
