package io.github.droidkaigi.confsched2022.data.auth
data class User(
    val idToken: String
)

interface Authenticator {
    suspend fun currentUser(): User?
    suspend fun signInAnonymously(): User?
}
