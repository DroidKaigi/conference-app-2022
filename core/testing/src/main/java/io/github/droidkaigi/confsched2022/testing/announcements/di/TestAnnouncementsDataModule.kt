package io.github.droidkaigi.confsched2022.testing.announcements.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched2022.data.announcement.FakeAnnouncementsRepository
import io.github.droidkaigi.confsched2022.data.announcements.di.AnnouncementsDataModule
import io.github.droidkaigi.confsched2022.model.AnnouncementsRepository

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AnnouncementsDataModule::class]
)
@Module
class TestAnnouncementsDataModule {
    @Provides
    fun provideAnnouncementsRepository(): AnnouncementsRepository {
        return FakeAnnouncementsRepository()
    }
}
