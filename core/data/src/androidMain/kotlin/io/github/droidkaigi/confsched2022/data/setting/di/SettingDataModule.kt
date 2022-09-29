package io.github.droidkaigi.confsched2022.data.setting.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.data.setting.DataDynamicColorSettingRepository
import io.github.droidkaigi.confsched2022.model.DynamicColorSettingRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
public class SettingDataModule {
    @Provides
    @Singleton
    public fun provideSessionsRepository(
        settingsDatastore: SettingsDatastore
    ): DynamicColorSettingRepository {
        return DataDynamicColorSettingRepository(
            settingsDatastore = settingsDatastore
        )
    }
}
