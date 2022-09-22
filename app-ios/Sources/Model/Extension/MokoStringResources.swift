public extension StringResource {
    func localized() -> String {
        return NSLocalizedString(resourceId, bundle: bundle, comment: "")
    }
}
