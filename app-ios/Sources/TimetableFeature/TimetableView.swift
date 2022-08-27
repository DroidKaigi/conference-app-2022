import appioscombined
import SwiftUI

enum TimetableItemType: Identifiable {
    case general(TimetableItem, Int)
    case spacing(Int)

    var id: String {
        switch self {
        case let .general(item, _):
            return item.title.jaTitle
        case .spacing:
            return UUID().uuidString
        }
    }
}

public struct TimetableView: View {
    static let minuteHeight: CGFloat = 4
    static let timetableStartTime: DateComponents = .init(hour: 10, minute: 0)

    private let dateComponentsFormatter: DateComponentsFormatter = {
        let formatter = DateComponentsFormatter()
        return formatter
    }()
    let timetable: Timetable

    @State var selectedDay: DroidKaigi2022Day = DroidKaigi2022Day.day1

    var hours: [Int] {
        Set(timetable.timetableItems
            .map(\.startsAt.epochSeconds)
            .map(Int.init)
            .map(TimeInterval.init)
            .map(Date.init(timeIntervalSince1970:))
            .map { Calendar.current.component(.hour, from: $0) })
            .sorted()
    }

    var roomTimetableItems: [(TimetableRoom, [TimetableItemType])] {
        let result = Set(timetable.timetableItems.map(\.room))
            .map { room -> (TimetableRoom, [TimetableItemType]) in
                var items = timetable.timetableItems
                    .filter {
                        $0.room == room
                    }
                    .reduce([TimetableItemType]()) { result, item in
                        var result = result
                        let lastItem = result.last
                        if case .general(let lItem, _) = lastItem, lItem.endsAt != item.startsAt {
                            result.append(.spacing(calculateMinute(
                                startSeconds: Int(lItem.endsAt.epochSeconds),
                                endSeconds: Int(item.startsAt.epochSeconds)
                            )))
                        }
                        let minute = calculateMinute(
                            startSeconds: Int(item.startsAt.epochSeconds),
                            endSeconds: Int(item.endsAt.epochSeconds)
                        )
                        result.append(
                            TimetableItemType.general(
                                item,
                                minute
                            )
                        )

                        return result
                    }
                if case let .general(firstItem, _) = items.first {
                    let hour = Calendar.current.component(.hour, from: firstItem.startsAt.toDate())
                    let minute = Calendar.current.component(.minute, from: firstItem.startsAt.toDate())
                    let firstSpacingItem: TimetableItemType = .spacing(minute + max(hour - hours.first!, 0) * 60)
                    items.insert(firstSpacingItem, at: 0)
                }

                return (room, items)
            }
            .sorted {
                $0.0.sort < $1.0.sort
            }

        return result
    }

    public init(timetable: Timetable) {
        self.timetable = timetable
    }

    public var body: some View {
        VStack {
            Picker(selection: $selectedDay) {
                ForEach([
                    DroidKaigi2022Day.day1,
                    DroidKaigi2022Day.day2,
                    DroidKaigi2022Day.day3,
                ]) { day in
                    Text(day.name).tag(day)
                }
            } label: {
                Text("Day select")
            }
            .pickerStyle(.segmented)

            ScrollView(.vertical) {
                HStack(alignment: .top) {
                    VStack(spacing: 0) {
                        Text("Room")
                        ForEach(hours, id: \.self) { hour in
                            Text(dateComponentsFormatter.string(from: DateComponents(hour: hour, minute: 0))!)
                                .frame(
                                    height: TimetableView.minuteHeight * 60,
                                    alignment: .top
                                )
                        }
                    }
                    ScrollView(.horizontal) {
                        HStack(alignment: .top) {
                            ForEach(roomTimetableItems, id: \.0) { (room, timetableItems) in
                                VStack(spacing: 0) {
                                    Text(room.name.jaTitle)
                                    ForEach(timetableItems, id: \.id) { item in
                                        if case let .general(item, minutes) = item {
                                            TimetableItemView(item: item)
                                                .frame(height: CGFloat(minutes) * TimetableView.minuteHeight)
                                                .background(randomColor())
                                        } else if case let .spacing(minutes) = item {
                                            Spacer()
                                                .frame(maxHeight: CGFloat(minutes) * TimetableView.minuteHeight)
                                        }
                                    }
                                }
                                .frame(width: 200)
                            }
                        }
                    }
                }
            }
        }
    }

    private func calculateMinute(startSeconds: Int, endSeconds: Int) -> Int {
        let startMinute = startSeconds / 60
        let endMinute = endSeconds / 60

        return endMinute - startMinute
    }
}

#if DEBUG
struct TimetableView_Previews: PreviewProvider {
    static var previews: some View {
        TimetableView(timetable: Timetable.companion.fake())
    }
}
#endif

func randomColor() -> Color {
    let red = Double.random(in: 0...1)
    let green = Double.random(in: 0...1)
    let blue = Double.random(in: 0...1)
    return Color(red: red, green: green, blue: blue)
}
extension TimetableItem: Identifiable {}
extension Kotlinx_datetimeInstant {
    func toDate() -> Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }
}
extension DroidKaigi2022Day: Identifiable {
    public var id: String {
        name
    }
}
