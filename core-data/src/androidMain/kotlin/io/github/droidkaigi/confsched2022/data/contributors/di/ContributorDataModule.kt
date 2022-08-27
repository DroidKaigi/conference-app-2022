package io.github.droidkaigi.confsched2022.data.contributors.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.contributors.FakeContributorsRepository
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ContributorDataModule {

    @Provides
    @Singleton
    fun provideContributorsRepository(
        networkService: NetworkService,
    ): ContributorsRepository {
        return FakeContributorsRepository()
    }
}
