import CommonComponents
import Model
import SwiftUI
import Theme

struct StaffRowView: View {
    let staff: Staff
    let onTap: () -> Void

    init(staff: Staff, onTap: @escaping () -> Void) {
        self.staff = staff
        self.onTap = onTap
    }
    var body: some View {
        Button {
            onTap()
        } label: {
            HStack {
                NetworkImage(url: URL(string: staff.iconUrl)) {
                    AnyView(
                        Circle()
                            .foregroundColor(AssetColors.onBackground.swiftUIColor)
                    )
                }
                .frame(width: 60, height: 60)
                .clipShape(Circle())
                .padding(.trailing, 16)
                Text(staff.username)
                    .font(Font.system(size: 16, weight: .medium, design: .default))
                    .font(.headline)
                Spacer()
            }
            .padding(16)
        }
    }
}

#if DEBUG
struct StaffRowView_Previews: PreviewProvider {
    static var previews: some View {
        StaffRowView(
            staff: Staff.companion.fakes().first!,
            onTap: {}
        )
    }
}
#endif
