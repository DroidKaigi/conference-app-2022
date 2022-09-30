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
        formatter.dateFormat = "YYYY EEEE d"
        return formatter
    }()
}

private extension DroidKaigi2022Day {
    // NOTE: This implementation is not good, but it is acceptable for this use case.
    func eventDate() -> String {
        let eventDateFormatter = DateFormatter.eventDateFormatter
        return "\(name) (\(eventDateFormatter.string(from: start.toDate()))th)"
    }
}

struct DayFilterSheetView: View {
    let days: [DroidKaigi2022Day]
    let selectedDay: DroidKaigi2022Day?
    let onClose: () -> Void
    let onTap: (DroidKaigi2022Day) -> Void

    var body: some View {
        VStack(alignment: .leading) {
            HStack(spacing: 16) {
                Button {
                    onClose()
                } label: {
                    Assets.close.swiftUIImage
                }
                Text(StringsKt.shared.search_filter_select_day.localized())
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(AssetColors.white.swiftUIColor)
                Spacer()
            }
            VStack(alignment: .leading, spacing: 16) {
                ForEach(days) { day in
                    DayFilterRadioButtonView(
                        day: day,
                        selected: selectedDay?.name == day.name,
                        onTap: { day in
                            onTap(day)
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

struct DayFilterRadioButtonView: View {
    let day: DroidKaigi2022Day
    let selected: Bool
    let onTap: @Sendable (DroidKaigi2022Day) -> Void
    var body: some View {
        HStack(spacing: 16) {
            Button {
                onTap(day)
            } label: {
                Image(systemName: selected ? "circle.inset.filled" : "circle")
                    .foregroundColor(AssetColors.onPrimaryContainer.swiftUIColor)
                Text(day.eventDate())
                    .foregroundColor(AssetColors.onBackground.swiftUIColor)
            }
        }
    }
}

#if DEBUG
struct DayFilterSheet_Previews: PreviewProvider {
    static let days = [DroidKaigi2022Day].fromKotlinArray(DroidKaigi2022Day.values())
    static var previews: some View {
        DayFilterSheetView(
            days: days,
            selectedDay: days[0],
            onClose: {},
            onTap: { _ in }
        )
        DayFilterSheetView(
            days: days,
            selectedDay: nil,
            onClose: {},
            onTap: { _ in }
        )
    }
}
#endif
