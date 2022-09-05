package io.github.droidkaigi.confsched2022

import io.github.droidkaigi.confsched2022.data.auth.Authenticator
import org.koin.core.Koin
import org.koin.core.context.startKoin
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun initKoin(authenticator: Authenticator): KoinApplication {
    return startKoin {
        modules(
            dataModule,
            module {
                single { authenticator }
            }
        )
    }
}

fun Koin.get(objCProtocol: ObjCProtocol): Any {
    val kClazz = getOriginalKotlinClass(objCProtocol)!!
    return get(kClazz)
}