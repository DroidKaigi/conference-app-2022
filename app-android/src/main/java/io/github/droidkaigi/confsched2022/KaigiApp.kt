package io.github.droidkaigi.confsched2022

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
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
import io.github.droidkaigi.confsched2022.impl.AndroidCalendarRegistration
import io.github.droidkaigi.confsched2022.impl.AndroidShareManager
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import io.github.droidkaigi.confsched2022.ui.LocalCalendarRegistration
import io.github.droidkaigi.confsched2022.ui.LocalShareManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiApp(
    windowSizeClass: WindowSizeClass,
    kaigiAppScaffoldState: KaigiAppScaffoldState = rememberKaigiAppScaffoldState(),
    kaigiExternalNavigationController: KaigiExternalNavigationController =
        rememberKaigiExternalNavigationController(),
) {
    KaigiTheme {
        CompositionLocalProvider(
            LocalShareManager provides AndroidShareManager(LocalContext.current),
            LocalCalendarRegistration provides AndroidCalendarRegistration(LocalContext.current),
        ) {
            val usePersistentNavigationDrawer = windowSizeClass.usePersistentNavigationDrawer
            KaigiAppDrawer(
                kaigiAppScaffoldState = kaigiAppScaffoldState,
                showPermanently = usePersistentNavigationDrawer,
                drawerSheetContent = {
                    DrawerSheetContent(
                        selectedDrawerItem = kaigiAppScaffoldState.selectedDrawerItem,
                        onClickDrawerItem = { drawerItem ->
                            kaigiAppScaffoldState.navigate(drawerItem)
                        }
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
                        showNavigationIcon,
                        kaigiAppScaffoldState::onNavigationClick,
                        kaigiAppScaffoldState::onBackIconClick,
                        kaigiAppScaffoldState::onSearchClick,
                        kaigiAppScaffoldState::onTimeTableClick,
                        kaigiAppScaffoldState::onNavigateFloorMapClick,
                    )
                    contributorsNavGraph(
                        showNavigationIcon,
                        kaigiAppScaffoldState::onNavigationClick,
                        onLinkClick = kaigiExternalNavigationController::navigate,
                    )
                    aboutNavGraph(
                        showNavigationIcon,
                        kaigiAppScaffoldState::onNavigationClick,
                        onLinkClick = kaigiExternalNavigationController::navigate,
                    )
                    mapGraph()
                    announcementGraph(
                        showNavigationIcon,
                        kaigiAppScaffoldState::onNavigationClick,
                    )
                }
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

@OptIn(ExperimentalMaterial3Api::class)
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
            content()
        }
    } else {
        ModalNavigationDrawer(
            drawerState = kaigiAppScaffoldState.drawerState,
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
            route = SessionsNavGraph.sessionSearchRoute()
        )
    }

    fun onNavigateFloorMapClick() {
        TODO("Floor map is not yet implemented.")
    }

    fun onNavigationClick() {
        coroutineScope.launch {
            drawerState.open()
        }
    }

    fun onBackIconClick() {
        navController.popBackStack()
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

enum class DrawerItem(val titleResId: Int, val icon: ImageVector, val navRoute: String) {
    Sessions(R.string.title_sessions, Icons.Default.Event, SessionsNavGraph.sessionRoute),
    About(R.string.title_about, Icons.Default.Android, AboutNavGraph.aboutRoute),
    Announcement(
        R.string.title_announcement,
        Icons.Default.Announcement,
        AnnouncementNavGraph.announcementRoute
    ),
    Map(R.string.title_map, Icons.Default.Map, MapNavGraph.mapRoute),
    Sponsors(R.string.title_sponsors, Icons.Default.Business, ""),
    Contributors(
        R.string.title_contributors,
        Icons.Default.People,
        ContributorsNavGraph.contributorsRoute
    ),
    Setting(R.string.title_setting, Icons.Default.Settings, "");

    companion object {
        fun ofOrNull(route: String): DrawerItem? {
            return values().firstOrNull { it.navRoute == route }
        }
    }
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
                    Text(stringResource(drawerItem.titleResId))
                },
                selected = drawerItem == selectedDrawerItem,
                onClick = {
                    onClickDrawerItem(drawerItem)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            if (drawerItem == DrawerItem.Sessions || drawerItem == DrawerItem.Map) {
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
    return remember(context) {
        KaigiExternalNavigationController(
            context,
        )
    }
}

class KaigiExternalNavigationController(
    private val context: Context,
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
}
