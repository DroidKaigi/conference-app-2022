import Assets
import Model
import SwiftUI
import Theme

struct SelectButtonView: View {
    let title: String
    let selected: Bool
    let onSelect: @Sendable () -> Void
    let onDeselect: @Sendable () -> Void
    var body: some View {
        HStack(spacing: 20) {
            Button {
                selected ? onDeselect() : onSelect()
            } label: {
                Group {
                    selected ?
                        Image(systemName: "checkmark.square.fill")
                            .resizable()
                            .frame(width: 18, height: 18)
                            .foregroundColor(AssetColors.primary.swiftUIColor)
                        :
                        Image(systemName: "square")
                            .resizable()
                            .frame(width: 18, height: 18)
                            .foregroundColor(AssetColors.primary.swiftUIColor)
                }
            }
            Text(title)
                .foregroundColor(AssetColors.onBackground.swiftUIColor)
                .font(.system(size: 16, weight: .medium))
        }
    }
}

struct SelectButtonView_Previews: PreviewProvider {
    static let category = TimetableCategory(
        id: 1,
        title: MultiLangText(jaTitle: "カテゴリー", enTitle: "Category")
    )
    static var previews: some View {
        VStack {
            SelectButtonView(
                title: category.title.currentLangTitle,
                selected: false,
                onSelect: {},
                onDeselect: {}
            )
            SelectButtonView(
                title: category.title.currentLangTitle,
                selected: true,
                onSelect: {},
                onDeselect: {}
            )
        }
    }
}
