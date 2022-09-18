package io.github.droidkaigi.confsched2022.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.droidkaigi.confsched2022.data.AndroidDriverFactory
import io.github.droidkaigi.confsched2022.data.DatabaseService
import io.github.droidkaigi.confsched2022.data.DriverFactory
import io.github.droidkaigi.confsched2022.data.sessions.SessionsDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
public class DaoModule {

    @Provides
    @Singleton
    public fun provideSessionsDao(
        databaseService: DatabaseService
    ): SessionsDao {
        return SessionsDao(databaseService)
    }

    @Provides
    @Singleton
    public fun provideDatabaseService(
        driverFactory: DriverFactory
    ): DatabaseService {
        return DatabaseService(driverFactory)
    }

    @Provides
    @Singleton
    public fun provideDriverFactory(
        @ApplicationContext context: Context
    ): DriverFactory {
        return AndroidDriverFactory(context)
    }
}
