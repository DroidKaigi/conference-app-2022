import appioscombined
import LicenseList
import Model
import SwiftUI

struct AboutLicenseView: View {
    private let url = Bundle.module.url(forResource: "license-list", withExtension: "plist")!

    var body: some View {
        LicenseListView(fileURL: url)
            .navigationTitle(StringsKt.shared.about_license.localized())
    }
}

#if DEBUG
struct AboutLicenseView_Previews: PreviewProvider {
    static var previews: some View {
        AboutLicenseView()
    }
}
#endif
