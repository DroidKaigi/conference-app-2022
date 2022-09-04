package io.github.droidkaigi.confsched2022

import org.koin.core.Koin
import org.koin.core.context.startKoin
import kotlinx.cinterop.ObjCProtocol
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.KoinApplication

fun initKoin(): KoinApplication {
    return startKoin {
        modules(dataModule)
    }
}

fun Koin.get(objCProtocol: ObjCProtocol): Any {
    val kClazz = getOriginalKotlinClass(objCProtocol)!!
    return get(kClazz)
}