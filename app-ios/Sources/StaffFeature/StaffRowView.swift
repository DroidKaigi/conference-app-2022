import Model
import SwiftUI

struct StaffRowView: View {
    let staff: Staff

    init(staff: Staff) {
        self.staff = staff
    }
    var body: some View {
        HStack {
            if let url = URL(string: staff.iconUrl) {
                AsyncImage(url: url)
                    .frame(width: 60, height: 60)
                    .clipShape(Circle())
                    .padding(.trailing, 16)
            } else {
                Circle()
                    .frame(width: 60, height: 60)
                    .foregroundColor(Color.white)
                    .padding(.trailing, 16)
            }
            Text(staff.username)
                .font(Font.system(size: 16, weight: .medium, design: .default))
                .font(.headline)
            Spacer()
        }
        .padding(16)
    }
}

struct StaffListItemView_Previews: PreviewProvider {
    static var previews: some View {
        StaffRowView(staff: Staff.companion.fakes().first!)
    }
}
