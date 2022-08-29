package io.github.droidkaigi.confsched2022.testing.sessions.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched2022.data.sessions.di.SessionDataModule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.testing.sessions.FakeSessionsRepository
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionDataModule::class]
)
@Module
class TestSessionDataModule {
    @Provides
    @Singleton
    fun provideSessionsRepository(): SessionsRepository {
        return FakeSessionsRepository()
    }
}
