import appioscombined
import Strings
import SwiftUI
import Theme

struct SessionSpeakersView: View {

    let speakers: [TimetableSpeaker]
    
    public var body: some View {
        VStack(alignment: .leading) {
            Text(L10n.Session.speaker)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)

            ForEach(self.speakers, id: \.self) { speaker in
                HStack {
                    AsyncImage(url: URL(string: speaker.iconUrl))
                        .clipShape(Circle())
                        .frame(width: 48, height: 48)
                        .padding(.trailing)
                    
                    Text(speaker.name)
                        .font(Font.system(size: 14, weight: .regular, design: .default))
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                }
            }
        }
    }
}
