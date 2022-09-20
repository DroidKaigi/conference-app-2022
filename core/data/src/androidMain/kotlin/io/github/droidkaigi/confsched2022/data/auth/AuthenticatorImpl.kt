package io.github.droidkaigi.confsched2022.data.auth

import co.touchlab.kermit.Logger
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import javax.inject.Inject

public class AuthenticatorImpl @Inject constructor() : Authenticator {
    override suspend fun currentUser(): User? {
        val firebaseUser = Firebase.auth.currentUser ?: return null
        val idToken = firebaseUser.getIdToken(false) ?: return null

        return User(idToken)
    }

    override suspend fun signInAnonymously(): User? {
        val result = Firebase.auth.signInAnonymously()
        Logger.d("signin:${result.user}")

        val firebaseUser = result.user ?: return null
        val idToken = firebaseUser.getIdToken(false) ?: return null

        return User(idToken)
    }
}
