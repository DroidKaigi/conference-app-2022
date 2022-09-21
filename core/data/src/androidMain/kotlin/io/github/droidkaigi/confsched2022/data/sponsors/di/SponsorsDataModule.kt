package io.github.droidkaigi.confsched2022.data.sponsors.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.sponsors.DataSponsorsRepository
import io.github.droidkaigi.confsched2022.data.sponsors.SponsorsApi
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
public class SponsorsDataModule {
    @Provides
    @Singleton
    public fun provideSponsorsApi(
        networkService: NetworkService
    ): SponsorsApi = SponsorsApi(networkService)

    @Provides
    @Singleton
    public fun provideSponsorsRepository(
        sponsorsApi: SponsorsApi
    ): SponsorsRepository = DataSponsorsRepository(sponsorsApi)
}
