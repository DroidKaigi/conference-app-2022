import appioscombined
import Assets
import Model
import SwiftUI
import Theme

private extension DateFormatter {
    static let eventDateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.calendar = Calendar(identifier: .gregorian)
        formatter.dateStyle = .long
        return formatter
    }()
}

private extension DroidKaigi2022Day {
    // NOTE: This implementation is not good, but it is acceptable for this use case.
    func eventDate() -> String {
        let eventDateFormatter = DateFormatter.eventDateFormatter
        let languageCode = Locale.current.language.languageCode?.identifier
        guard let languageCode = languageCode else { return name }
        if languageCode == "ja" {
            eventDateFormatter.dateFormat = "YYYY年MM月d日"
            return "\(name) (\(eventDateFormatter.string(from: start.toDate())))"
        }
        if languageCode == "en" {
            eventDateFormatter.dateFormat = "YYYY EEEE d"
            return "\(name) (\(eventDateFormatter.string(from: start.toDate()))th)"
        }
        return name
    }
}

struct DayFilterSheetView: View {
    let days: [DroidKaigi2022Day]
    let selectedDays: [DroidKaigi2022Day]
    let onClose: () -> Void
    let onSelect: (DroidKaigi2022Day) -> Void
    let onDeselect: (DroidKaigi2022Day) -> Void

    var body: some View {
        VStack(alignment: .leading) {
            FilterSheetHeaderView(title: StringsKt.shared.search_filter_select_category.localized()) {
                   onClose()
            }
            .padding(.bottom, 20)
            VStack(alignment: .leading, spacing: 16) {
                ForEach(days) { day in
                    SelectButtonView(
                        title: day.eventDate(),
                        selected: selectedDays.contains(day),
                        onSelect: {
                            onSelect(day)
                        },
                        onDeselect: {
                           onDeselect(day)
                        }
                    )
                }
            }
        }
        .padding(.vertical, 20)
        .padding(.horizontal, 24)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(AssetColors.surface.swiftUIColor)
    }
}

#if DEBUG
struct DayFilterSheet_Previews: PreviewProvider {
    static let days = [DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values())
    static var previews: some View {
        DayFilterSheetView(
            days: days,
            selectedDays: days,
            onClose: {  },
            onSelect: { _ in },
            onDeselect: { _ in }
        )
        DayFilterSheetView(
            days: days,
            selectedDays: [],
            onClose: {  },
            onSelect: { _ in },
            onDeselect: { _ in }
        )
    }
}
#endif
