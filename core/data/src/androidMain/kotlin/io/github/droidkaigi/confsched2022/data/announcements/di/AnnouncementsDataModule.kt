package io.github.droidkaigi.confsched2022.data.announcements.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.announcement.AnnouncementsApi
import io.github.droidkaigi.confsched2022.data.announcement.DataAnnouncementRepository
import io.github.droidkaigi.confsched2022.model.AnnouncementRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
public class AnnouncementsDataModule {

    @Provides
    @Singleton
    public fun provideAnnouncementsApi(networkService: NetworkService): AnnouncementsApi {
        return AnnouncementsApi(networkService)
    }

    @Provides
    @Singleton
    public fun provideAnnouncementsRepository(
        announcementsApi: AnnouncementsApi
    ): AnnouncementRepository {
        return DataAnnouncementRepository(announcementsApi)
    }
}
