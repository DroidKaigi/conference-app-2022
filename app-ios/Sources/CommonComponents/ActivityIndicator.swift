import SwiftUI

public struct ActivityIndicator: UIViewRepresentable {

    private let activityIndicatorView = UIActivityIndicatorView(style: .large)

    private let style: UIActivityIndicatorView.Style
    private var color: Color

    public init(
        style: UIActivityIndicatorView.Style = .large,
        color: Color = Color.gray
    ) {
        self.style = style
        self.color = color
    }

    public func makeUIView(context: UIViewRepresentableContext<ActivityIndicator>) -> UIActivityIndicatorView {
        self.activityIndicatorView.style = self.style
        self.activityIndicatorView.color = UIColor(self.color)
        return self.activityIndicatorView
    }

    public func updateUIView(_ uiView: UIActivityIndicatorView, context: UIViewRepresentableContext<ActivityIndicator>) {
        uiView.startAnimating()
    }
}

#if DEBUG
struct ActivityIndicator_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            ActivityIndicator()
            ActivityIndicator(style: .medium)
            ActivityIndicator(color: .red)
            ActivityIndicator(color: .green)
            ActivityIndicator(color: .blue)
        }
    }
}
#endif
