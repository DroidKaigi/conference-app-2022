package io.github.droidkaigi.confsched2022

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.github.droidkaigi.confsched2022.data.NetworkService
import io.github.droidkaigi.confsched2022.data.auth.AuthApi
import io.github.droidkaigi.confsched2022.data.SettingsDatastore
import io.github.droidkaigi.confsched2022.data.sessions.DataSessionsRepository
import io.github.droidkaigi.confsched2022.data.sessions.SessionsApi
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

val dataModule = module {
    singleOf<FlowSettings> {
        AppleSettings(NSUserDefaults.new()!!).toFlowSettings()
    }
    singleOf(::SettingsDatastore)
    singleOf(::NetworkService)
    singleOf(::AuthApi)
    singleOf(::SessionsApi)
    singleOf(::DataSessionsRepository) bind SessionsRepository::class
}
