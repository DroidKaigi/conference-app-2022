package io.github.droidkaigi.confsched2022.testing.zipline.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched2022.testing.zipline.FakeSessionsZipline
import io.github.droidkaigi.confsched2022.zipline.SessionsZipline
import io.github.droidkaigi.confsched2022.zipline.di.SessionsZiplineModule
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionsZiplineModule::class]
)
@Module
public class TestSessionsZiplineModule {

    @Provides
    @Singleton
    internal fun provideSessionsZipline(): SessionsZipline {
        return FakeSessionsZipline()
    }
}
