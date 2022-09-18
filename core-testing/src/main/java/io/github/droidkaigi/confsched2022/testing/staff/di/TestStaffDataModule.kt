package io.github.droidkaigi.confsched2022.testing.staff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.github.droidkaigi.confsched2022.data.staff.FakeStaffRepository
import io.github.droidkaigi.confsched2022.data.staff.di.StaffDataModule
import io.github.droidkaigi.confsched2022.model.StaffRepository

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StaffDataModule::class]
)
@Module
class TestStaffDataModule {
    @Provides
    fun provideContributorsRepository(): StaffRepository {
        return FakeStaffRepository()
    }
}
