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
            HStack(spacing: 20) {
                Button {
                    onClose()
                } label: {
                    Assets.close.swiftUIImage
                }
                Text(StringsKt.shared.search_filter_select_category.localized())
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(AssetColors.white.swiftUIColor)
                Spacer()
            }
            VStack(alignment: .leading, spacing: 24) {
                ForEach(categories, id: \.self) { category in
                    CategoryFilterSelectButtonView(
                        category: category,
                        selected: selectedCategories.contains(category),
                        onSelect: { category in
                            onSelect(category)
                        },
                        onDeselect: { category in
                            onDeselect(category)
                        }
                    )
                }
                Spacer()
            }
            .padding(.top, 24)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .padding(32)
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
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                    :
                    Image(systemName: "square")
                        .resizable()
                        .frame(width: 18, height: 18)
                        .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
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
    static let selectedCategories = Array(1...5).map { TimetableCategory(id: $0, title: MultiLangText(jaTitle: "カテゴリー \($0)", enTitle: "Category \($0)"))}
    static var previews: some View {
        VStack {
            CategoryFilterSheetView(
                categories: categories,
                selectedCategories: selectedCategories,
                onDeselect: { _ in}, onSelect: { _ in },
                onClose: {}
            )
            .background(AssetColors.secondaryContainer.swiftUIColor)
        }
    }
}
#endif
