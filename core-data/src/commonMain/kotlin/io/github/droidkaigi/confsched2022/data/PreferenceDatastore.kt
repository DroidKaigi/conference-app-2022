package io.github.droidkaigi.confsched2022.data

import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.Flow
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

    companion object {
        val NAME = "PREFERENCES_NAME"
        val KEY = "favorites"
        val DELIMITER = ","
    }
}
