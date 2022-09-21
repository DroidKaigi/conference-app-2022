import SwiftUI

struct ScrollDetector: View {

    private struct ScrollPreferenceKey: PreferenceKey {
        static var defaultValue: CGRect = .zero
        static func reduce(value: inout CGRect, nextValue: () -> CGRect) {
            value = nextValue()
        }
    }

    let coordinateSpace: CoordinateSpace
    var onDetect: (CGPoint) -> Void

    init(coordinateSpace: CoordinateSpace, onDetect: @escaping (CGPoint) -> Void = { _ in }) {
        self.coordinateSpace = coordinateSpace
        self.onDetect = onDetect
    }

    var body: some View {
        GeometryReader { proxy in
            VStack {
            }
            .preference(key: ScrollPreferenceKey.self, value: proxy.frame(in: coordinateSpace))
        }
        .onPreferenceChange(ScrollPreferenceKey.self) {
            onDetect($0.origin)
        }
    }

    func onDetect(perform action: @escaping (CGPoint) -> Void) -> some View {
        var view = self
        view.onDetect = action
        return view
    }
}

protocol ScrollDetectable {
    var scrollThreshold: CGFloat { get set }
    var onScroll: (CGPoint) -> Void { get set }
}
extension ScrollDetectable where Self: View {

    func scrollThreshold(_ value: CGFloat) -> Self {
        var view = self
        view.scrollThreshold = value
        return view
    }

    func onScroll(perform action: @escaping (CGPoint) -> Void) -> Self {
        var view = self
        view.onScroll = action
        return view
    }
}
