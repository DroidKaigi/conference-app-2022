import appioscombined
import SwiftUI
import Strings

struct SessionAssetsView: View {
    @Environment(\.openURL) var openURL
    
    let asset: TimetableAsset
    
    public var body: some View {
        VStack {
            Text(L10n.Session.material)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)
            if let videoUrl = self.asset.videoUrl {
                HStack {
                    Image(systemName: "video")
                        .padding(.trailing, 8)
                    Button {
                        self.openURL(URL(string: videoUrl)!)
                    } label: {
                        Text(L10n.Session.movie)
                            .font(Font.system(size: 14, weight: .regular, design: .default))
                    }
                }
                .padding(.bottom)
            }
            if let slidesUrl = self.asset.slideUrl {
                HStack {
                    Image(systemName: "photo.on.rectangle")
                        .padding(.trailing, 8)
                    Button {
                        self.openURL(URL(string: slidesUrl)!)
                    } label: {
                        Text(L10n.Session.slides)
                            .font(Font.system(size: 14, weight: .regular, design: .default))
                    }
                }
            }
        }
    }
}
