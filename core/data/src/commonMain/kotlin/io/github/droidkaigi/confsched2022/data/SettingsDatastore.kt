package io.github.droidkaigi.confsched2022.data

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

public class SettingsDatastore(private val flowSettings: FlowSettings) {

    public suspend fun addFavorite(sessionId: String) {
        val favoriteSet = flowSettings.getString(KEY, "").toFavoriteSet().toMutableSet()
        favoriteSet.add(sessionId)
        flowSettings.putString(KEY, favoriteSet.toValue())
    }

    public suspend fun removeFavorite(sessionId: String) {
        val favoriteSet = flowSettings.getString(KEY, "").toFavoriteSet().toMutableSet()
        favoriteSet.remove(sessionId)
        flowSettings.putString(KEY, favoriteSet.toValue())
    }

    public fun favoriteSessionIds(): Flow<ImmutableSet<String>> {
        return flowSettings.getStringFlow(KEY, "")
            .map { it.toFavoriteSet() }
    }

    private fun String.toFavoriteSet(): ImmutableSet<String> {
        return this.split(DELIMITER).filter { it.isNotBlank() }.toImmutableSet()
    }

    private fun Set<String>.toValue(): String {
        return this.joinToString(DELIMITER)
    }

    public fun isAuthenticated(): Flow<Boolean?> {
        return flowSettings.getBooleanOrNullFlow(KEY_AUTHENTICATED)
    }

    public suspend fun setAuthenticated(authenticated: Boolean) {
        flowSettings.putBoolean(
            KEY_AUTHENTICATED,
            authenticated,
        )
    }

    private val mutableIdToken = MutableStateFlow<String?>(null)
    public val idToken: StateFlow<String?> = mutableIdToken
    public suspend fun setIdToken(token: String): Unit = mutableIdToken.emit(token)

    public fun deviceId(): Flow<String?> {
        return flowSettings.getStringOrNullFlow(KEY_DEVICE_ID)
    }

    public suspend fun setDeviceId(deviceId: String) {
        flowSettings.putString(
            KEY_DEVICE_ID,
            deviceId
        )
    }

    public fun dynamicColorEnabled(): Flow<Boolean> {
        return flowSettings.getBooleanFlow(KEY_DYNAMIC_COLOR, false)
    }

    public suspend fun setDynamicColorEnabled(dynamicColorEnabled: Boolean) {
        flowSettings.putBoolean(
            KEY_DYNAMIC_COLOR,
            dynamicColorEnabled,
        )
    }

    public companion object {
        public const val NAME: String = "PREFERENCES_NAME"
        private const val KEY_AUTHENTICATED = "KEY_AUTHENTICATED"
        private const val KEY_DEVICE_ID = "KEY_DEVICE_ID"
        private const val KEY_DYNAMIC_COLOR = "KEY_DYNAMIC_COLOR"
        private const val KEY = "favorites"
        private const val DELIMITER = ","
    }
}
