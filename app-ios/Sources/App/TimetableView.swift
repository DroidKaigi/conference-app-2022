import appioscombined
import SwiftUI

enum TimetableItemType: Identifiable {
    case general(TimetableItem, Int)
    case spacing(Int)

    var id: String {
        switch self {
        case let .general(item, _):
            return item.title.jaTitle
        case .spacing(_):
            return UUID().uuidString
        }
    }
}

public struct TimetableView: View {
    static let minuteHeight: CGFloat = 4

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
        let rooms = Set(timetable.timetableItems.map(\.room))
        return rooms
            .map { room -> (TimetableRoom, [TimetableItemType]) in
                let items = timetable.timetableItems
                    .filter {
                        $0.room == room
                    }
                    .reduce([TimetableItemType]()) { result, item in
                        var result = result
                        let lastItem = result.last
                        if result.isEmpty {
                            let minute = Calendar.current.component(.minute, from: item.startsAt.toDate())
                            result.append(.spacing(minute))
                        }
                        if case .general(let lItem, _) = lastItem, lItem.endsAt != item.startsAt {
                            let lastEndSec = Int(lItem.endsAt.epochSeconds)
                            let lastEndMinute = lastEndSec / 60
                            let nextStartMinute = Int(item.startsAt.epochSeconds) / 60
                            let spacingMinute = nextStartMinute - lastEndMinute
                            result.append(.spacing(spacingMinute))
                        }
                        let endsMinute = item.endsAt.epochSeconds / 60
                        let startsMinute = item.startsAt.epochSeconds / 60
                        result.append(.general(item, Int(endsMinute - startsMinute)))

                        return result
                    }
                return (room, items)
            }
            .sorted { a, b in
                return a.0.sort < b.0.sort
            }
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
                        ForEach(hours, id: \.self) { hour in
                            Text("\(hour):00")
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
}

#if DEBUG
struct _Previews: PreviewProvider {
    static var previews: some View {
        TimetableView(timetable: Timetable.companion.fake())
    }
}
#endif

func randomColor() -> Color {
    let red = Double.random(in: 0...1)
    let green = Double.random(in: 0...1)
    let blue = Double.random(in: 0...1)
    print("\(red), \(green), \(blue)")
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
