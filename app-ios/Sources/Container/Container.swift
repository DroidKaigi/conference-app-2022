import appioscombined

public struct DIContainer {
    private let koin: Koin_coreKoin

    public init(authenticator: Authenticator) {
        koin = KmpHelperKt.doInitKoin(authenticator: authenticator).koin
    }

    public func get<TypeProtocol, ReturnType>(type: TypeProtocol) -> ReturnType where TypeProtocol: Protocol {
        guard let object = koin.get(objCProtocol: type) as? ReturnType else {
            fatalError("Not found instance for \(type)")
        }
        return object
    }
}
