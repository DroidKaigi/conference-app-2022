# DroidKaigi 2022 official app

## Development

### Features

### Design

### Try it out

TBD

### Contributing
We always welcome any and all contributions! See CONTRIBUTING.md for more information

For Japanese speakers, please see CONTRIBUTING.ja.md

## Requirements

Latest Android Studio **Android Studio Electric Eel | 2022.1.1 Canary 9** and higher. You can download it from [this page](https://developer.android.com/studio/preview).
[iOS Requirements](app-ios/README.md)

## Tech Stacks

This year's app pretty much takes the idea from [now in android](https://github.com/android/nowinandroid) and adds a lot of ideas to it.

### Configurable build logic
The build logic uses a division similar to that used in module division, such as feature-xxx and core-xxx.

Two types of build plugins are included.

* primitive plugins

It includes several simple plugins, such as the Android plugin and the Compose plugin, as shown below.
Even simple plugins require dependencies among plugins, which can be configured by indicating such dependencies in the package path.
For example, `android.hilt` requires an `android` plugin.

```
droidkaigi.primitive.android
droidkaigi.primitive.android.compose
droidkaigi.primitive.android.hilt
droidkaigi.primitive.kmp
droidkaigi.primitive.kmp.ios
```
* convention plugins

```kotlin
class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("droidkaigi.primitive.android")
                apply("droidkaigi.primitive.android.kotlin")
                apply("droidkaigi.primitive.android.compose")
                apply("droidkaigi.primitive.android.hilt")
                apply("droidkaigi.primitive.spotless")
                apply("droidkaigi.primitive.molecule")
            }
        }
    }
}
```

### Instant logic updates using Kotlin JS
We are trying to use [https://github.com/cashapp/zipline](cashapp/zipline) as an experimental approach.
This allows us to use the regular JVM Kotlin implementation as a fallback, while releasing logic implemented in Javascript, which can be updated instantly as well as development on the Web.
We are excited about these possibilities for Kotlin.

The following interface is implemented in Kotlin JS and Android.
You can add session announcements, etc. here. Since this is an experimental trial, it does not have such a practical role this time.

```kotlin
interface ScheduleModifier : ZiplineService {
    suspend fun modify(
        schedule: DroidKaigiSchedule
    ): DroidKaigiSchedule
}
```

```kotlin
class AndroidScheduleModifier : ScheduleModifier {
    override suspend fun modify(schedule: DroidKaigiSchedule): DroidKaigiSchedule {
        return schedule
    }
}
```

```kotlin
class JsScheduleModifier() : ScheduleModifier {
    override suspend fun modify(schedule: DroidKaigiSchedule): DroidKaigiSchedule {
...
    if (timetableItem is Session &&
        timetableItem.id == TimetableItemId("1")
    ) {
        timetableItem.copy(
            message = MultiLangText(
                enTitle = "This is a js message",
                jaTitle = "これはJSからのメッセージ",
            )
        )
    } else {
        timetableItem
    }
...
    }
}
```

### Building a UiModel using Compose

[https://github.com/cashapp/molecule](cashapp/molecule) is used to create the UiModel.
Jetpack Compose allows reactive streams such as Flow to be easily handled by Recompose. This also allows you to create an initial State.

```kotlin
val uiModel = moleculeScope.moleculeComposeState(clock = ContextClock) {
    val scheduleResult by scheduleResultFlow.collectAsState(initial = Result.Loading)

    val scheduleState by remember {
        derivedStateOf {
            val scheduleState = ScheduleState.of(scheduleResult)
            scheduleState.filter(filters.value)
        }
    }
    SessionsUiModel(scheduleState = scheduleState, isFilterOn = filters.value.filterFavorite)
}
```

### Create a test with high Fidelity without making it Flaky

In this project, we will use Hilt in the JVM for integration testing to avoid emulator-specific problems.
We also use the Robot testing pattern to separate the how and what of testing, making it scalable.

```kotlin
@Test
fun canToggleFavorite() {
    sessionScreenRobot(robotTestRule) {
        clickFavoriteAt(0)
        checkFavoritedAt(index = 0, isFavorited = true)
        checkFavoriteIsSavedAt(0)
    }
}
```
