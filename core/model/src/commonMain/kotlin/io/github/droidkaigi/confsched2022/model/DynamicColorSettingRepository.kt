package io.github.droidkaigi.confsched2022.model

import kotlinx.coroutines.flow.Flow

public interface DynamicColorSettingRepository {
    public fun dynamicEnabledFlow(): Flow<Boolean>
    public suspend fun setDynamicColorEnabled(dynamicColorEnabled: Boolean)
}
