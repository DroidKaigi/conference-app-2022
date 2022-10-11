package io.github.droidkaigi.confsched2022.zipline

import app.cash.zipline.EventListener
import app.cash.zipline.loader.LoadResult.Failure
import app.cash.zipline.loader.LoadResult.Success
import app.cash.zipline.loader.ManifestVerifier
import app.cash.zipline.loader.ZiplineLoader
import co.touchlab.kermit.Logger
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import java.util.concurrent.Executors
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

public interface SessionsZipline {
    public fun timetableModifier(): Flow<suspend (DroidKaigiSchedule) -> DroidKaigiSchedule>
}

internal class SessionsZiplineImpl(
    okHttpClient: OkHttpClient
) : SessionsZipline {
    private val executorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val dispatcher = executorService.asCoroutineDispatcher()

    private val manifestUrl = "https://droidkaigi.github.io/conference-app-2022/" +
        "manifest.zipline.json"

    private val ziplineLoader = ZiplineLoader(
        dispatcher = dispatcher,
        manifestVerifier = ManifestVerifier.NO_SIGNATURE_CHECKS,
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
                exception: Exception,
                startValue: Any?,
            ) {
                Logger.d(exception) { "Zipline applicationLoadFailed" }
            }

            override fun downloadFailed(
                applicationName: String,
                url: String,
                exception: Exception,
                startValue: Any?,
            ) {
                Logger.d(exception) { "Zipline downloadFailed" }
            }
        },
        nowEpochMs = { System.currentTimeMillis() },
    )

    // Limit the write access only on the Zipline thread
    private val mutex = Mutex()
    private var cachedScheduleModifier: ScheduleModifier? = null
    private suspend fun takeOrGetScheduleModifier(): ScheduleModifier = withContext(dispatcher) {
        mutex.withLock {
            val cached = cachedScheduleModifier
            if (cached != null) {
                return@withLock cached
            }
            val ziplineLoadResult = ziplineLoader.loadOnce(
                applicationName = "timeline",
                manifestUrl = manifestUrl,
                initializer = { },
            )
            when (ziplineLoadResult) {
                is Success -> {
                    val taken = ziplineLoadResult.zipline.take<ScheduleModifier>("sessionsModifier")
                    cachedScheduleModifier = taken
                    taken
                }
                is Failure -> {
                    throw ziplineLoadResult.exception
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class) // measureTimedValue
    override fun timetableModifier(): Flow<suspend (DroidKaigiSchedule) -> DroidKaigiSchedule> {
        return channelFlow {
            // The loaded JsScheduleModifier takes about 300 ms to execute.
            // Therefore, if a cached loaded JsScheduleModifier is emitted first,
            // the UI will show loading while the JsScheduleModifier is running.
            // Prevent this by emitting the AndroidScheduleModifier first, even if it is cached.
            val androidScheduleModifier = AndroidScheduleModifier()
            val defaultModifier: suspend (DroidKaigiSchedule) -> DroidKaigiSchedule = { timetable ->
                Logger.v("zipline Android")
                androidScheduleModifier.modify(timetable)
            }
            send(defaultModifier)

            try {
                val scheduleModifier = takeOrGetScheduleModifier()
                send { timetable ->
                    Logger.v("zipline JS executing")
                    val timedValue = measureTimedValue { scheduleModifier.modify(timetable) }
                    Logger.v("zipline JS took ${timedValue.duration}")
                    timedValue.value
                }
            } catch (e: Exception) {
                Logger.d(e) { "zipline load error" }
            }
        }
    }
}
