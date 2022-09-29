import appioscombined
import Model
import SwiftUI
import Theme

struct AnnouncementsByDateView: View {

    let announcementsByDate: AnnouncementsByDate

    var body: some View {
        LazyVStack(alignment: .leading, spacing: 24) {
            Text(announcementsByDate.publishedAt.toRelativeDateString())
                .font(.system(size: 22, weight: .medium))
                .padding(.top, 24)
            ForEach(announcementsByDate.announcements, id: \.id) { announcement in
                VStack(alignment: .leading, spacing: 24) {
                    AnnouncementItemView(announcement: announcement)
                    if announcement.id == announcementsByDate.announcements.last?.id {
                        Divider().background(AssetColors.outline.swiftUIColor)
                            .frame(height: 1)
                    }
                }
            }
        }
        .padding(.horizontal, 16)
    }
}

#if DEBUG
struct AnnouncementsByDateView_Previews: PreviewProvider {
    static var previews: some View {
        AnnouncementsByDateView(announcementsByDate: AnnouncementsByDate.companion.fakes()[0])
    }
}
#endif
