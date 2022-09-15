import SwiftUI
import Strings

struct SessionAudienceView: View {

    let targetAudience: String

    var body: some View {
        VStack(alignment: .leading) {
            Text(L10n.Session.targetAudience)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)
            Text(self.targetAudience)
                .font(Font.system(size: 14, weight: .regular, design: .default))
                .lineSpacing(4)
        }
    }
}
