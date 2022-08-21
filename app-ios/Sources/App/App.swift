import appioscombined
import SwiftUI

struct Session: Identifiable {
    var type: SessionType
    var title: String
    var author: String
    var startTime: DateComponents
    var endTime: DateComponents
    var blocks: Int

    var id: String {
        UUID().uuidString
    }
}

enum SessionType {
    case general
    case spacing
}

let sessions: [Session] = [
    .init(
        type: .general,
        title: "オープニング",
        author: "ry-itto",
        startTime: .init(hour: 10, minute: 0),
        endTime: .init(hour: 10, minute: 15),
        blocks: 1
    ),
    .init(
        type: .spacing,
        title: "",
        author: "",
        startTime: .init(hour: 10, minute: 15),
        endTime: .init(hour: 11, minute: 0),
        blocks: 3
    ),
    .init(
        type: .general,
        title: "セッション1",
        author: "ry-itto",
        startTime: .init(hour: 11, minute: 0),
        endTime: .init(hour: 11, minute: 45),
        blocks: 3
    ),
    .init(
        type: .spacing,
        title: "",
        author: "",
        startTime: .init(hour: 11, minute: 45),
        endTime: .init(hour: 12, minute: 0),
        blocks: 1
    ),
]

func randomColor() -> Color {

    let red = Double.random(in: 0...1)
    let green = Double.random(in: 0...1)
    let blue = Double.random(in: 0...1)
    print("\(red), \(green), \(blue)")
    return Color(red: red, green: green, blue: blue)
}

public struct ContentView: View {
    public init() {}

    let minutes = [
        0,
        15,
        30,
        45,
    ]
    let hours = [
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
    ]

    var dates: [(Int, [Int])] {
        hours.map {
            ($0, minutes)
        }
    }

    public var body: some View {
        let blockHeight: CGFloat = 80
        ScrollView(.vertical) {
            ScrollView(.horizontal) {
                HStack(alignment: .top) {
                    VStack(spacing: 0) {
//                        Text("")
                        ForEach(dates, id: \.0) { (a, b) in
                            ForEach(b, id: \.self) { min in
                                if min == 0 {
                                    Text("\(a):\(min)0")
                                        .frame(height: blockHeight)
                                } else {
                                    Text("\(a):\(min)")
                                        .frame(height: blockHeight)
//                                        .opacity(0)
                                }
                            }
                        }
                    }
                    ForEach(["A", "B", "C", "D"], id: \.self) { room in
                        VStack(spacing: 0) {
                            Text("Room \(room)")
                            ForEach(sessions) { session in
                                VStack(alignment: .leading) {
                                    switch session.type {
                                    case .general:
                                        Text(session.title)
                                        Spacer()
                                        Text("\(session.startTime) - \(session.endTime)")
                                        Text("\(session.author)")
                                    case .spacing:
                                        EmptyView()
                                    }
                                }
                                .padding()
                                .frame(maxHeight: blockHeight * CGFloat(session.blocks))
                                .background(session.type == .general ? randomColor() : Color.white)
                            }
                        }
                        .frame(width: 200)
                    }
                }
            }
        }
    }
}

#if DEBUG
public struct ContentView_Previews: PreviewProvider {
    public static var previews: some View {
        ContentView()
    }
}
#endif
