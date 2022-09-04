package io.github.droidkaigi.confsched2022.zipline

import android.app.Application
import app.cash.zipline.EventListener
import app.cash.zipline.Zipline
import app.cash.zipline.loader.ZiplineLoader
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class SessionsZipline @Inject constructor(
    context: Application,
    okHttpClient: OkHttpClient
) {
    private val executorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val dispatcher = executorService.asCoroutineDispatcher()

    private val manifestUrl = "https://droidkaigi.github.io/conference-app-2022/manifest.zipline.json"

    private val ziplineLoader = ZiplineLoader(
        context = context,
        dispatcher = dispatcher,
        httpClient = okHttpClient,
        eventListener = object : EventListener() {
            override fun manifestParseFailed(
                applicationName: String,
                url: String?,
                exception: Exception
            ) {
                Logger.d(exception) { "Zipline manifestParseFailed" }
            }

            override fun applicationLoadFailed(
                applicationName: String,
                manifestUrl: String?,
                exception: Exception
            ) {
                Logger.d(exception) { "Zipline applicationLoadFailed" }
            }

            override fun downloadFailed(
                applicationName: String,
                url: String,
                exception: Exception
            ) {
                Logger.d(exception) { "Zipline downloadFailed" }
            }
        },
        nowEpochMs = { System.currentTimeMillis() }
    )

    fun timetableModifier(
        coroutineScope: CoroutineScope,
    ): MutableStateFlow<suspend (DroidKaigiSchedule) -> DroidKaigiSchedule> {
        val androidScheduleModifier = AndroidScheduleModifier()
        val defaultModifier: suspend (DroidKaigiSchedule) -> DroidKaigiSchedule = { timetable ->
            androidScheduleModifier.modify(timetable)
        }
        val modifierStateFlow = MutableStateFlow(defaultModifier)

        coroutineScope.launch(dispatcher) {
            var zipline: Zipline? = null
//            val modifier =
            // If the server works, we will comment in
            val modifier = try {
                val loadedZiplineFlow = ziplineLoader.load("timeline", flowOf(manifestUrl), { })
                loadedZiplineFlow.catch { throwable -> throwable.printStackTrace() }
                val loadedZipline = loadedZiplineFlow.firstOrNull()
                if (loadedZipline == null) {
                    loadedZiplineFlow.catch { it.printStackTrace() }
                }
                zipline = loadedZipline!!.zipline
                zipline.take<ScheduleModifier>("sessionsModifier")
            } catch (e: Exception) {
                Logger.d(e) { "zipline load error" }
                androidScheduleModifier
            }
            modifierStateFlow.emit { timetable -> modifier.modify(timetable) }

            coroutineContext.job.invokeOnCompletion {
                dispatcher.dispatch(EmptyCoroutineContext) { zipline?.close() }
                executorService.shutdown()
            }
        }
        return modifierStateFlow
    }
}
