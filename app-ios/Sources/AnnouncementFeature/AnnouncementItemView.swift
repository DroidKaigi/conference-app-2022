import appioscombined
import Assets
import SwiftUI

struct AnnouncementItemView: View {

    let announcement: Announcement

    var body: some View {
        HStack(alignment: .top, spacing: 0) {
            Image(announcement.type, bundle: .myModule)
                .resizable()
                .frame(width: 16, height: 16)
            Spacer().frame(width: 8)
            VStack(alignment: .leading) {
                Text(announcement.title)
                    .font(.system(size: 16, weight: .semibold))
                Spacer().frame(height: 8)
                Text(announcement.content)
                    .font(.system(size: 16, weight: .regular))
            }
        }
    }
}

#if DEBUG
struct AnnouncementItemView_Previews: PreviewProvider {
    static var previews: some View {
        AnnouncementItemView(announcement: AnnouncementsByDate.companion.fakes()[0].announcements[0])
    }
}
#endif
