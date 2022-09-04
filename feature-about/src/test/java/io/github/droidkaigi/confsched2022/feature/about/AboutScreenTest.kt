package io.github.droidkaigi.confsched2022.feature.about

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class AboutScreenTest {
    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var aboutScreenRobot: AboutScreenRobot

    @Test
    fun checkAboutVisible() {
        aboutScreenRobot(robotTestRule) {
            checkAboutVisible()
        }
    }
}