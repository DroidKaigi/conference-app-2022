package io.github.droidkaigi.confsched2022

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.about.AboutNavGraph
import io.github.droidkaigi.confsched2022.feature.about.aboutNavGraph
import io.github.droidkaigi.confsched2022.feature.announcement.AnnouncementNavGraph
import io.github.droidkaigi.confsched2022.feature.announcement.announcementGraph
import io.github.droidkaigi.confsched2022.feature.contributors.ContributorsNavGraph
import io.github.droidkaigi.confsched2022.feature.contributors.contributorsNavGraph
import io.github.droidkaigi.confsched2022.feature.map.MapNavGraph
import io.github.droidkaigi.confsched2022.feature.map.mapGraph
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsNavGraph
import io.github.droidkaigi.confsched2022.feature.sessions.sessionsNavGraph
import io.github.droidkaigi.confsched2022.feature.setting.AppUiModel
import io.github.droidkaigi.confsched2022.feature.setting.KaigiAppViewModel
import io.github.droidkaigi.confsched2022.feature.setting.SettingNavGraph
import io.github.droidkaigi.confsched2022.feature.setting.settingNavGraph
import io.github.droidkaigi.confsched2022.feature.sponsors.SponsorsNavGraph
import io.github.droidkaigi.confsched2022.feature.sponsors.sponsorsNavGraph
import io.github.droidkaigi.confsched2022.feature.staff.StaffNavGraph
import io.github.droidkaigi.confsched2022.feature.staff.staffNavGraph
import io.github.droidkaigi.confsched2022.impl.AndroidCalendarRegistration
import io.github.droidkaigi.confsched2022.impl.AndroidShareManager
import io.github.droidkaigi.confsched2022.model.TimetableCategory
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.strings.Strings
import io.github.droidkaigi.confsched2022.ui.CalendarRegistration
import io.github.droidkaigi.confsched2022.ui.ShareManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiApp(
    windowSizeClass: WindowSizeClass,
    kaigiAppViewModel: KaigiAppViewModel = hiltViewModel(),
    kaigiAppScaffoldState: KaigiAppScaffoldState = rememberKaigiAppScaffoldState(),
    kaigiExternalNavigationController: KaigiExternalNavigationController =
        rememberKaigiExternalNavigationController(),
) {
    val appUiModel: AppUiModel by kaigiAppViewModel.uiModel

    KaigiTheme(isDynamicColorEnabled = appUiModel.isDynamicColorEnabled) {
        val usePersistentNavigationDrawer = windowSizeClass.usePersistentNavigationDrawer
        KaigiAppDrawer(
            kaigiAppScaffoldState = kaigiAppScaffoldState,
            showPermanently = usePersistentNavigationDrawer,
            drawerSheetContent = {
                DrawerSheetContent(
                    selectedDrawerItem = kaigiAppScaffoldState.selectedDrawerItem,
                    onClickDrawerItem = kaigiAppScaffoldState::navigate,
                )
            }
        ) {
            NavHost(
                modifier = Modifier,
                navController = kaigiAppScaffoldState.navController,
                startDestination = SessionsNavGraph.sessionRoute,
            ) {
                val showNavigationIcon = !usePersistentNavigationDrawer
                sessionsNavGraph(
                    showNavigationIcon = showNavigationIcon,
                    onLinkClick = kaigiExternalNavigationController::navigate,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                    onCategoryTagClick = kaigiAppScaffoldState::onCategoryTagClick,
                    onBackIconClick = kaigiAppScaffoldState::onBackIconClick,
                    onSearchIconClick = kaigiAppScaffoldState::onSearchClick,
                    onTimetableClick = kaigiAppScaffoldState::onTimeTableClick,
                    onShareClick = kaigiExternalNavigationController::onShareClick,
                    onNavigateFloorMapClick = kaigiAppScaffoldState::onNavigateFloorMapClick,
                    onRegisterCalendarClick =
                    kaigiExternalNavigationController::onRegisterCalendarClick
                )
                contributorsNavGraph(
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                    onLinkClick = kaigiExternalNavigationController::navigate,
                )
                aboutNavGraph(
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                    onLinkClick = kaigiExternalNavigationController::navigate,
                    onStaffListClick = kaigiAppScaffoldState::onStaffListClick,
                    onLicenseClick = kaigiExternalNavigationController::navigateToOssLicense,
                )
                staffNavGraph(
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                    onLinkClick = kaigiExternalNavigationController::navigate,
                )
                mapGraph(
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                )
                announcementGraph(
                    showNavigationIcon = showNavigationIcon,
                    onLinkClick = kaigiExternalNavigationController::navigate,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                )
                settingNavGraph(
                    appUiModel = appUiModel,
                    showNavigationIcon = true,
                    onDynamicColorToggle = kaigiAppViewModel::onDynamicColorToggle,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick
                )
                sponsorsNavGraph(
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = kaigiAppScaffoldState::onNavigationClick,
                    onItemClick = kaigiExternalNavigationController::navigate
                )
            }
        }
    }
}

private val WindowSizeClass.usePersistentNavigationDrawer: Boolean
    get() = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> false
        WindowWidthSizeClass.Medium -> false
        WindowWidthSizeClass.Expanded -> true
        else -> false
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberKaigiAppScaffoldState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = Closed)
): KaigiAppScaffoldState {
    return remember(navController, coroutineScope, drawerState) {
        KaigiAppScaffoldState(
            coroutineScope,
            navController,
            drawerState
        )
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun KaigiAppDrawer(
    kaigiAppScaffoldState: KaigiAppScaffoldState = rememberKaigiAppScaffoldState(),
    showPermanently: Boolean,
    drawerSheetContent: @Composable ColumnScope.() -> Unit,
    content: @Composable () -> Unit
) {
    if (showPermanently) {
        PermanentNavigationDrawer(
            drawerContent = { PermanentDrawerSheet { drawerSheetContent() } },
        ) {
            Box(
                modifier = Modifier.consumedWindowInsets(
                    WindowInsets.systemBars.only(WindowInsetsSides.Start)
                )
            ) {
                content()
            }
        }
    } else {
        val keyboardController = LocalSoftwareKeyboardController.current
        val drawerState = kaigiAppScaffoldState.drawerState
        LaunchedEffect(drawerState.isAnimationRunning) {
            if (drawerState.isAnimationRunning && drawerState.isClosed) {
                keyboardController?.hide()
            }
        }
        BackHandler(enabled = drawerState.isOpen) {
            kaigiAppScaffoldState.coroutineScope.launch {
                drawerState.close()
            }
        }
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { ModalDrawerSheet { drawerSheetContent() } },
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class KaigiAppScaffoldState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val coroutineScope: CoroutineScope,
    val navController: NavHostController,
    val drawerState: DrawerState,
) {
    fun navigate(drawerItem: DrawerItem) {
        navController.navigate(drawerItem.navRoute)
        coroutineScope.launch {
            drawerState.close()
        }
    }

    fun onTimeTableClick(timetableId: TimetableItemId) {
        navController.navigate(
            route = SessionsNavGraph.sessionDetailRoute(timetableId.value)
        )
    }

    fun onSearchClick() {
        navController.navigate(
            route = SessionsNavGraph.sessionSearchRoute(null)
        )
    }

    fun onCategoryTagClick(
        category: TimetableCategory?
    ) {
        navController.navigate(
            route = SessionsNavGraph.sessionSearchRoute(category?.id.toString())
        )
    }

    fun onNavigateFloorMapClick() {
        navController.navigate(
            route = MapNavGraph.mapRoute
        )
    }

    fun onNavigationClick() {
        coroutineScope.launch {
            drawerState.open()
        }
    }

    fun onBackIconClick() {
        navController.popBackStack()
    }

    fun onStaffListClick() {
        navController.navigate(StaffNavGraph.staffRoute)
    }

    private var _selectedDrawerItem: MutableState<DrawerItem?> = mutableStateOf<DrawerItem?>(null)
    val selectedDrawerItem get() = _selectedDrawerItem.value

    init {
        coroutineScope.launch {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                _selectedDrawerItem.value = destination.route?.let { DrawerItem.ofOrNull(it) }
            }
        }
    }
}

enum class DrawerGroup {
    Session, Information, Others
}

enum class DrawerItem(
    val group: DrawerGroup,
    val titleStringRes: StringResource,
    val icon: ImageVector,
    val navRoute: String
) {
    Sessions(
        DrawerGroup.Session,
        Strings.title_sessions,
        Icons.Default.Event,
        SessionsNavGraph.sessionRoute
    ),
    About(
        DrawerGroup.Information,
        Strings.title_about,
        Icons.Default.Android,
        AboutNavGraph.aboutRoute
    ),
    Announcement(
        DrawerGroup.Information,
        Strings.title_announcement,
        Icons.Default.Announcement,
        AnnouncementNavGraph.announcementRoute
    ),
    Map(DrawerGroup.Information, Strings.title_map, Icons.Default.Map, MapNavGraph.mapRoute),
    Sponsors(
        DrawerGroup.Others,
        Strings.title_sponsors,
        Icons.Default.Business,
        SponsorsNavGraph.sponsorsRoute
    ),
    Contributors(
        DrawerGroup.Others,
        Strings.title_contributors,
        Icons.Default.People,
        ContributorsNavGraph.contributorsRoute
    ),
    Setting(
        DrawerGroup.Others,
        Strings.title_setting,
        Icons.Default.Settings,
        SettingNavGraph.settingRoute
    );

    companion object {
        fun ofOrNull(route: String): DrawerItem? {
            return values().firstOrNull { it.navRoute == route }
        }
    }

    val isLastItem: Boolean
        get() = ordinal == DrawerItem.values().lastIndex

    val isGroupLastItem: Boolean
        get() = isLastItem || group != DrawerItem.values()[ordinal + 1].group
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.DrawerSheetContent(
    selectedDrawerItem: DrawerItem?,
    onClickDrawerItem: (DrawerItem) -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.img_navigation_drawer_lookup),
        contentDescription = null,
        modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp)
    )
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        DrawerItem.values().forEach { drawerItem ->
            NavigationDrawerItem(
                icon = {
                    Icon(imageVector = drawerItem.icon, contentDescription = null)
                },
                label = {
                    Text(stringResource(drawerItem.titleStringRes))
                },
                selected = drawerItem == selectedDrawerItem,
                onClick = {
                    onClickDrawerItem(drawerItem)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            if (!drawerItem.isLastItem && drawerItem.isGroupLastItem) {
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun rememberKaigiExternalNavigationController(): KaigiExternalNavigationController {
    val context = LocalContext.current
    val shareManager = AndroidShareManager(context)
    val calendarRegistration = AndroidCalendarRegistration(context)

    return remember(context) {
        KaigiExternalNavigationController(
            context,
            shareManager,
            calendarRegistration
        )
    }
}

class KaigiExternalNavigationController(
    private val context: Context,
    private val shareManager: ShareManager,
    private val calendarRegistration: CalendarRegistration
) {

    fun navigate(
        url: String,
        packageName: String? = null,
    ) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            if (packageName != null) {
                intent.setPackage(packageName)
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            navigateToCustomTab(
                url = url,
                context = context,
            )
        }
    }

    private fun navigateToCustomTab(url: String, context: Context) {
        val uri = Uri.parse(url)
        CustomTabsIntent.Builder().also { builder ->
            builder.setShowTitle(true)
            builder.build().also {
                it.launchUrl(context, uri)
            }
        }
    }

    fun navigateToOssLicense() {
        context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }

    fun onShareClick(timeTableItem: TimetableItem) {
        shareManager.share(
            "${timeTableItem.title.currentLangTitle}\n" +
                "https://droidkaigi.jp/2022/timetable/${timeTableItem.id.value}"
        )
    }

    fun onRegisterCalendarClick(timeTableItem: TimetableItem) {
        calendarRegistration.register(
            startsAtMilliSeconds = timeTableItem.startsAt.toEpochMilliseconds(),
            endsAtMilliSeconds = timeTableItem.endsAt.toEpochMilliseconds(),
            title = timeTableItem.title.currentLangTitle,
            location = timeTableItem.room.name.currentLangTitle,
        )
    }
}
