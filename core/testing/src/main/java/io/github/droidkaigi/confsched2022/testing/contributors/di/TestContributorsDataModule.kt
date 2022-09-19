package io.github.droidkaigi.confsched2022.testing.contributors.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched2022.data.contributors.FakeContributorsRepository
import io.github.droidkaigi.confsched2022.data.contributors.di.ContributorDataModule
import io.github.droidkaigi.confsched2022.model.ContributorsRepository

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ContributorDataModule::class]
)
@Module
class TestContributorsDataModule {
    @Provides
    fun provideContributorsRepository(): ContributorsRepository {
        return FakeContributorsRepository()
    }
}
