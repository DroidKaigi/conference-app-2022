import appioscombined

extension Kotlinx_datetimeInstant {
    public func toDate() -> Date {
        Date(timeIntervalSince1970: TimeInterval(epochSeconds))
    }
}
