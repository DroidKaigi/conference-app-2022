package io.github.droidkaigi.confsched2022

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.droidkaigi.confsched2022.data.DatabaseService
import io.github.droidkaigi.confsched2022.data.DriverFactory
import io.github.droidkaigi.confsched2022.data.NativeDriverFactory
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.data.announcement.AnnouncementsApi
import io.github.droidkaigi.confsched2022.data.announcement.DataAnnouncementsRepository
import io.github.droidkaigi.confsched2022.data.auth.AuthApi
import io.github.droidkaigi.confsched2022.data.contributors.ContributorsApi
import io.github.droidkaigi.confsched2022.data.contributors.DataContributorsRepository
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.data.sessions.SessionsDao
import io.github.droidkaigi.confsched2022.data.sessions.defaultKtorConfig
import io.github.droidkaigi.confsched2022.data.sponsors.DataSponsorsRepository
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.SponsorsRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

public val dataModule: Module = module {
    singleOf<FlowSettings> {
        AppleSettings(NSUserDefaults.new()!!).toFlowSettings()
    }
    singleOf(::SettingsDatastore)
    single {
        HttpClient(Darwin) {
            engine {}
            defaultKtorConfig(get())
        }
    }
    singleOf(::NetworkService)
    singleOf(::AuthApi)
    singleOf(::SessionsApi)
    singleOf(::ContributorsApi)
    singleOf(::AnnouncementsApi)
    singleOf<DriverFactory>(::NativeDriverFactory)
    singleOf(::DatabaseService)
    singleOf(::SessionsDao)
    singleOf(::DataContributorsRepository) bind ContributorsRepository::class
    singleOf(::DataSessionsRepository) bind SessionsRepository::class
    singleOf(::DataSponsorsRepository) bind SponsorsRepository::class
    singleOf(::DataAnnouncementsRepository) bind DataAnnouncementsRepository::class
}
