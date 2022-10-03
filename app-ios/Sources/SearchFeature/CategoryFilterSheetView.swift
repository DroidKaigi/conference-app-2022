import appioscombined
import Assets
import Model
import SwiftUI
import Theme

struct CategoryFilterSheetView: View {
    let categories: [TimetableCategory]
    let selectedCategories: [TimetableCategory]
    let onDeselect: (TimetableCategory) -> Void
    let onSelect: (TimetableCategory) -> Void
    let onClose: () -> Void
    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            FilterSheetHeaderView(title: StringsKt.shared.search_filter_select_category.localized()) {
                   onClose()
            }
            VStack(alignment: .leading, spacing: 24) {
                ForEach(categories, id: \.self) { category in
                    SelectButtonView(
                        title: category.title.currentLangTitle,
                        selected: selectedCategories.contains(category),
                        onSelect: {
                            onSelect(category)
                        },
                        onDeselect: {
                           onDeselect(category)
                        }
                    )
                }
                Spacer()
            }
            .padding(.top, 24)
            .padding(.horizontal, 8)
        }
        .padding(.vertical, 20)
        .padding(.horizontal, 24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.surface.swiftUIColor)
    }
}

struct CategoryFilterSelectButtonView: View {
    let category: TimetableCategory
    let selected: Bool
    let onSelect: @Sendable (TimetableCategory) -> Void
    let onDeselect: @Sendable (TimetableCategory) -> Void
    var body: some View {
        HStack(spacing: 20) {
            Button {
                selected ? onDeselect(category) : onSelect(category)
            } label: {
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
            Text(category.title.currentLangTitle)
                .foregroundColor(AssetColors.onBackground.swiftUIColor)
                .font(.system(size: 16, weight: .medium))
        }
    }
}

#if DEBUG
struct CategoryFilterSheet_Previews: PreviewProvider {
    static let categories = Array(1...10).map { TimetableCategory(id: $0, title: MultiLangText(jaTitle: "カテゴリー \($0)", enTitle: "Category \($0)"))}
    static var previews: some View {
        CategoryFilterSheetView(
            categories: categories,
            selectedCategories: Array(categories[0...4]),
            onDeselect: { _ in }, onSelect: { _ in },
            onClose: {}
        )
        .background(AssetColors.secondaryContainer.swiftUIColor)
    }
}
#endif
