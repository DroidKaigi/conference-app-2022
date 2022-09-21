import appioscombined
import CommonComponents
import SwiftUI
import Theme

struct SessionSpeakersView: View {

    let speakers: [TimetableSpeaker]

    var body: some View {
        VStack(alignment: .leading) {
            Text(StringsKt.shared.session_speaker.desc().localized())
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)

            ForEach(self.speakers, id: \.self) { speaker in
                HStack {
                    NetworkImage(
                        url: URL(string: speaker.iconUrl)
                    )
                    .frame(width: 60, height: 60)
                    .clipShape(Circle())
                    .padding(.trailing)
                    Text(speaker.name)
                        .font(Font.system(size: 16, weight: .regular, design: .default))
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                }
            }
        }
    }
}

#if DEBUG
struct SessionSpeakersView_Previews: PreviewProvider {
    static var previews: some View {
        SessionSpeakersView(speakers: TimetableItem.Session.companion.fake().speakers)
    }
}
#endif
