package io.github.droidkaigi.confsched2022.data.staff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.staff.DataStaffRepository
import io.github.droidkaigi.confsched2022.data.staff.StaffApi
import io.github.droidkaigi.confsched2022.model.StaffRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class StaffDataModule {

    @Provides
    @Singleton
    fun provideStaffApi(networkService: NetworkService): StaffApi {
        return StaffApi(networkService)
    }

    @Provides
    @Singleton
    fun provideStaffRepository(
        staffApi: StaffApi
    ): StaffRepository {
        return DataStaffRepository(staffApi)
    }
}
