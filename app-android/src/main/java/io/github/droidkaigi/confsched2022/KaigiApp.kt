package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.droidkaigi.confsched2022.designsystem.theme.KaigiTheme
import io.github.droidkaigi.confsched2022.feature.contributors.ContributorsNavGraph
import io.github.droidkaigi.confsched2022.feature.contributors.contributorsNavGraph
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsNavGraph
import io.github.droidkaigi.confsched2022.feature.sessions.sessionsNavGraph
import io.github.droidkaigi.confsched2022.model.TimetableItemId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KaigiApp(
    calculateWindowSizeClass: WindowSizeClass,
    kaigiAppScaffoldState: KaigiAppScaffoldState = rememberKaigiAppScaffoldState()
) {
    KaigiTheme {
        KaigiAppDrawer(
            kaigiAppScaffoldState = kaigiAppScaffoldState,
            drawerSheet = {
                DrawerSheet(
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
                sessionsNavGraph(
                    kaigiAppScaffoldState::onNavigationClick,
                    kaigiAppScaffoldState::onTimeTableClick,
                )
                contributorsNavGraph(kaigiAppScaffoldState::onNavigationClick)
            }
        }
    }
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
    drawerSheet: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Vertical
                )
            ),
        drawerState = kaigiAppScaffoldState.drawerState,
        drawerContent = {
            drawerSheet()
        }
    ) {
        content()
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
            route = SessionsNavGraph.sessionDetail + timetableId.value
        )
    }

    fun onNavigationClick() {
        coroutineScope.launch {
            drawerState.open()
        }
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

enum class DrawerItem(val title: String, val navRoute: String) {
    Sessions("Sessions", SessionsNavGraph.sessionRoute),
    Contributors("Contributors", ContributorsNavGraph.contributorsRoute);

    companion object {
        fun ofOrNull(route: String): DrawerItem? {
            return values().firstOrNull { it.navRoute == route }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerSheet(
    selectedDrawerItem: DrawerItem?,
    onClickDrawerItem: (DrawerItem) -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        DrawerItem.values().forEach { drawerItem ->
            NavigationDrawerItem(
                label = {
                    Text(drawerItem.title)
                },
                selected = drawerItem == selectedDrawerItem,
                onClick = {
                    onClickDrawerItem(drawerItem)
                }
            )
        }
    }
}
