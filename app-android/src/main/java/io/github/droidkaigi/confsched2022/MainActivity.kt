package io.github.droidkaigi.confsched2022

import android.os.Bundle
import android.graphics.Color
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Color.TRANSPARENT
            } else {
                Color.argb((255 * 0.5).toInt(), 0, 0, 0)
            }

        setContent {
            KaigiApp(calculateWindowSizeClass(this))
        }
    }
}
