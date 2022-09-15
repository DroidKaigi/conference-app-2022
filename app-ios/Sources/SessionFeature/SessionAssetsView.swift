import appioscombined
import Assets
import SwiftUI
import Strings

struct SessionAssetsView: View {
    @Environment(\.openURL) var openURL

    let asset: TimetableAsset

    var body: some View {
        VStack {
            Text(L10n.Session.material)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)
            if let videoUrl = self.asset.videoUrl {
                HStack {
                    Assets.video.swiftUIImage
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
                    Assets.slides.swiftUIImage
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

#if DEBUG
struct SessionAssetsView_Previews: PreviewProvider {
    static var previews: some View {
        SessionAssetsView(asset: TimetableItem.Session.companion.fake().asset)
    }
}
#endif
