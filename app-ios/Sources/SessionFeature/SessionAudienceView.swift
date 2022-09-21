import appioscombined
import SwiftUI

struct SessionAudienceView: View {

    let targetAudience: String

    var body: some View {
        VStack(alignment: .leading) {
            Text(StringsKt.shared.session_target_audience.desc().localized())
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)
            Text(self.targetAudience)
                .font(Font.system(size: 14, weight: .regular, design: .default))
                .lineSpacing(4)
        }
    }
}
