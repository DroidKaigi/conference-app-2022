import SwiftUI

public struct SessionTimeView: View {
    private var startsAt: Date
    private var endsAt: Date

    public init(startsAt: Date, endsAt: Date) {
        self.startsAt = startsAt
        self.endsAt = endsAt
    }

    public var body: some View {
        VStack {
            Text(startsAt.sessionTimeString())
                .font(Font(UIFont.systemFont(ofSize: 16, weight: .bold)))
                .frame(height: 24)
            Rectangle()
                .frame(width: 1, height: 4)
            Text(endsAt.sessionTimeString())
                .font(Font(UIFont.systemFont(ofSize: 16, weight: .bold)))
                .frame(height: 24)
        }
    }
}
