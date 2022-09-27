package io.github.droidkaigi.confsched2022.data.setting

import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.model.DynamicColorSettingRepository
import kotlinx.coroutines.flow.Flow

public class DataDynamicColorSettingRepository(
    private val settingsDatastore: SettingsDatastore
): DynamicColorSettingRepository {
    override fun dynamicEnabledFlow(): Flow<Boolean> {
        return settingsDatastore.dynamicColorEnabled()
    }

    override suspend fun setDynamicColorEnabled(dynamicColorEnabled: Boolean) {
        settingsDatastore.setDynamicColorEnabled(dynamicColorEnabled = dynamicColorEnabled)
    }
}
