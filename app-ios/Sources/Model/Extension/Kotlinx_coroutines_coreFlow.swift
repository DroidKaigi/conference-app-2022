import appioscombined

class FlowCollector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback: (T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let value = value as? T {
            callback(value)
        }
        completionHandler(nil)
    }
}

public extension Kotlinx_coroutines_coreFlow {
    func collect<T>() async throws -> T {
        return try await withCheckedThrowingContinuation { [weak self] continuation in
            self?.collect(collector: FlowCollector<T>(
                callback: { value in
                    continuation.resume(returning: value)
                }
            ), completionHandler: { error in
                if let error = error {
                    continuation.resume(throwing: error)
                }
            })
        }
    }
}
