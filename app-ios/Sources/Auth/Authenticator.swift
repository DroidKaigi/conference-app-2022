import appioscombined
import Firebase
import FirebaseAuth

public class Authenticator: appioscombined.Authenticator {
    public init() {
        FirebaseApp.configure()
    }

    public func currentUser() async throws -> appioscombined.User? {
        let idTokenResult = try await Auth.auth().currentUser?.getIDTokenResult()
        return idTokenResult.map(\.token).map(appioscombined.User.init)
    }

    public func signInAnonymously() async throws -> appioscombined.User? {
        let result = try await Auth.auth().signInAnonymously()
        let idTokenResult = try await result.user.getIDTokenResult()
        return appioscombined.User(idToken: idTokenResult.token)
    }
}
