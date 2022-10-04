package io.github.droidkaigi.confsched2022

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.droidkaigi.confsched2022.strings.Strings
import android.Manifest as AndroidManifest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(
                this,
                getString(Strings.notification_not_granted_message.resourceId),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        askNotificationPermissionIfNeeded()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Color.TRANSPARENT
            } else {
                // Make it easy to see navigation buttons for older OS versions
                Color.argb((255 * 0.5).toInt(), 0, 0, 0)
            }

        val sessionIdString: String? = if (this.intent?.data != null) {
            this.intent.data.toString()
        } else {
            null
        }

        setContent {
            KaigiApp(
                windowSizeClass = calculateWindowSizeClass(this),
                sessionIdFromNotification = sessionIdString
            )
        }
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (
            ContextCompat.checkSelfPermission(
                this,
                AndroidManifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // can post notifications
        } else if (
            shouldShowRequestPermissionRationale(AndroidManifest.permission.POST_NOTIFICATIONS)
        ) {
            /*
             * display an educational UI explaining to the user the features that will be enabled
             * by them granting the POST_NOTIFICATION permission if needed.
             */
        } else {
            requestPermissionLauncher.launch(AndroidManifest.permission.POST_NOTIFICATIONS)
        }
    }
}
