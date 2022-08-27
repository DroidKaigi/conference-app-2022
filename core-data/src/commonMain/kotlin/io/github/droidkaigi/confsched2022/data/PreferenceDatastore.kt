package io.github.droidkaigi.confsched2022.data

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class PreferenceDatastore(private val flowSettings: FlowSettings) {

    suspend fun addFavorite(sessionId: String) {
        val favoriteSet = flowSettings.getString(KEY, "").toFavoriteSet().toMutableSet()
        favoriteSet.add(sessionId)
        flowSettings.putString(KEY, favoriteSet.toValue())
    }

    suspend fun removeFavorite(sessionId: String) {
        val favoriteSet = flowSettings.getString(KEY, "").toFavoriteSet().toMutableSet()
        favoriteSet.remove(sessionId)
        flowSettings.putString(KEY, favoriteSet.toValue())
    }

    fun favoriteSessionIds(): Flow<ImmutableSet<String>> {
        return flowSettings.getStringFlow(KEY, "")
            .map { it.toFavoriteSet() }
    }

    private fun String.toFavoriteSet(): ImmutableSet<String> {
        return this.split(DELIMITER).filter { it.isNotBlank() }.toImmutableSet()
    }

    private fun Set<String>.toValue(): String {
        return this.joinToString(DELIMITER)
    }

    fun isAuthenticated(): Flow<Boolean?> {
        return flowSettings.getBooleanOrNullFlow(KEY_AUTHENTICATED)
    }

    suspend fun setAuthenticated(authenticated: Boolean) {
        flowSettings.putBoolean(
            KEY_AUTHENTICATED,
            authenticated,
        )
    }

    private val mutableIdToken = MutableStateFlow<String?>(null)
    val idToken: StateFlow<String?> = mutableIdToken
    suspend fun setIdToken(token: String) = mutableIdToken.emit(token)

    fun deviceId(): Flow<String?> {
        return flowSettings.getStringOrNullFlow(KEY_DEVICE_ID)
    }

    suspend fun setDeviceId(deviceId: String) {
        flowSettings.putString(
            KEY_DEVICE_ID,
            deviceId
        )
    }

    companion object {
        public const val NAME = "PREFERENCES_NAME"
        private const val KEY_AUTHENTICATED = "KEY_AUTHENTICATED"
        private const val KEY_DEVICE_ID = "KEY_DEVICE_ID"
        private const val KEY = "favorites"
        private const val DELIMITER = ","
    }
}
