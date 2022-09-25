import appioscombined
import Assets
import Model
import SwiftUI

struct AnnouncementItemView: View {

    let announcement: Announcement

    var body: some View {
        HStack(alignment: .top, spacing: 0) {
            announcement.typeImage
                .resizable()
                .frame(width: 16, height: 16)
            Spacer().frame(width: 8)
            VStack(alignment: .leading, spacing: 8) {
                Text(announcement.title)
                    .font(.system(size: 16, weight: .semibold))
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

private extension Announcement {
    var typeImage: Image {
        switch AnnouncementType(rawValue: type) ?? .notification {
        case .alert:
            return Assets.alert.swiftUIImage
        case .notification:
            return Assets.notification.swiftUIImage
        case .feedback:
            return Assets.feedback.swiftUIImage
        }
    }
}
