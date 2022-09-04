# DroidKaigi 2022 official app

## Development

### Features

| top | drawer |
|---|---|
| <img src="https://user-images.githubusercontent.com/1386930/188312501-4bd5ca1c-30db-48f9-9e78-3a9decb394de.png" width="250px" /> | <img src="https://user-images.githubusercontent.com/1386930/188312398-e89c4fd0-dd3a-4499-8975-35650a531c93.png" width="250px" /> |


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

<img width="891" alt="image" src="https://user-images.githubusercontent.com/1386930/188314552-a12ff0bb-851e-465c-a51c-679a7c711e37.png">


### Configurable build logic
The build logic uses a division similar to that used in module division, such as feature-xxx and core-xxx.

Two types of build plugins are included.

* Primitive plugins

It includes several simple plugins, such as the Android plugin and the Compose plugin, as shown below.
Even simple plugins require dependencies among plugins, which can be configured by indicating such dependencies in the package path.
For example, `android.hilt` requires an `android` plugin.

```kotlin
plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
}
```

* Convention plugins

The Convention plugin for this project is written by combining several primitive plugins.   
In this way, the plugin is configurable.

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

### Testing strategy


#### Make test scalable by using robot testing pattern

In this project, tests are separated into what and how.
This makes the tests scalable, as there is no need to rewrite many tests when the Compose mechanism, layout, etc. changes.

The test describes what is to be tested.

```kotlin
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SessionsScreenTest {

    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var sessionScreenRobot: SessionScreenRobot

    @Test
    fun canToggleFavorite() {
        sessionScreenRobot(robotTestRule) {
            clickFavoriteAt(0)
            checkFavoritedAt(index = 0, isFavorited = true)
            checkFavoriteIsSavedAt(0)
        }
    }
}
```

Robot describes how to test it. It therefore contains implementation details. There is no need to look at this code when adding tests on a regular basis.

```kotlin
class SessionScreenRobot @Inject constructor() {
    ...

    context(RobotTestRule)
    fun clickFavoriteAt(index: Int) {
        composeTestRule
            .onFavorite(
                index
            )
            .performClick()
    }

    private fun AndroidComposeTestRule<*, *>.onFavorite(index: Int): SemanticsNodeInteraction {
        val title = DroidKaigiSchedule.fake().itemAt(index)
            .title
            .currentLangTitle

        return onNode(
            matcher = hasTestTag("favorite") and hasAnySibling(hasText(title)),
            useUnmergedTree = true
        )
    }
...
```


#### Create a test with high fidelity without making it flaky

In this project, we will use Hilt in the JVM for integration testing to avoid device-specific problems.  
We believe that the more we use the same classes as the actual production application, the better the test will be able to catch real problems. Therefore, we use production dependencies as much as possible with Hilt.　　
The test basically uses the actual dependencies and Fake the Repository, which is the point of contact with the outside world.

```kotlin
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SessionDataModule::class] // replace the production Module
)
@Module
class TestSessionDataModule {
    @Provides
    fun provideSessionsRepository(): SessionsRepository {
        return FakeSessionsRepository()
    }
}
```

Expose fields that are not used in the app only in the Fake repository to allow some testing. Here we see that Favorite has been saved.

```kotlin
class FakeSessionsRepository : SessionsRepository {
    private val favorites = MutableStateFlow(persistentSetOf<TimetableItemId>())
    
    // ...
    
    // for test
    val savedFavorites get(): PersistentSet<TimetableItemId> = favorites.value
}
```

```kotlin
class SessionScreenRobot @Inject constructor() {
    @Inject lateinit var sessionsRepository: SessionsRepository
    private val fakeSessionsRepository: FakeSessionsRepository
        get() = sessionsRepository as FakeSessionsRepository

    fun checkFavoriteIsSavedAt(index: Int) {
        val expected = DroidKaigiSchedule.fake().itemAt(index).id
        fakeSessionsRepository.savedFavorites shouldContain expected
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

You can check the manifest file to see how it works.　　
https://droidkaigi.github.io/conference-app-2022/manifest.zipline.json


### LazyLayout

We are trying to draw a timetable using LazyLayout, a base implementation of LazyColumn and LazyGrid, which was introduced in [the Lazy layouts in Compose session](https://www.youtube.com/watch?v=1ANt65eoNhQ) at Google I/O.
<img src="https://user-images.githubusercontent.com/1386930/188336755-596d505e-cd62-4791-abdc-f32cbb4a39a7.png" width="250" />

https://github.com/DroidKaigi/conference-app-2022/blob/91715b461b3162eb04ac58b79ba39ccdf21cf222/feature-sessions/src/main/java/io/github/droidkaigi/confsched2022/feature/sessions/Timetable.kt#L73
