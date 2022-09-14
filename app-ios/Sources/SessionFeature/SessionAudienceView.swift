import SwiftUI
import Strings

struct SessionAudienceView: View {
    
    let targetAudience: String
    
    public var body: some View {
        VStack {
            Text(L10n.Session.targetAudience)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .padding(.bottom)
            Text(self.targetAudience)
                .font(Font.system(size: 14, weight: .regular, design: .default))
                .lineSpacing(4)
        }
    }
}
