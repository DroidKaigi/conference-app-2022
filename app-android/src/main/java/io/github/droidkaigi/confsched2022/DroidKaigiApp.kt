package io.github.droidkaigi.confsched2022

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue.Closed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.droidkaigi.confsched2022.designsystem.theme.DroidKaigiTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DroidKaigiApp(
    calculateWindowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    DroidKaigiTheme {
        val drawerState = rememberDrawerState(initialValue = Closed)
        val coroutineScope = rememberCoroutineScope()

        ModalNavigationDrawer(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Vertical
                    )
                ),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    (0..10).forEach {
                        NavigationDrawerItem(
                            label = {
                                Text("Session$it")
                            },
                            selected = true,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        )
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    SmallTopAppBar(
                        title = {
                            Text("DroidKaigi")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.open()
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Menu, "menu")
                            }
                        }
                    )
                }
            ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                ) {
                    NavHost(
                        modifier = Modifier
                            .padding(padding),
                        navController = navController,
                        startDestination = "sessions"
                    ) {
                        composable(route = "sessions") {
                            SessionsScreenRoot()
                        }
                    }
                }
            }
        }
    }
}
