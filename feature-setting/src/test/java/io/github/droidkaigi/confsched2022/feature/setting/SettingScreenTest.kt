package io.github.droidkaigi.confsched2022.feature.setting

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SettingScreenTest {
    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var settingScreenRobot: SettingScreenRobot

    @Test
    fun checkVisible() {
        settingScreenRobot(robotTestRule) {
            checkSettingVisible()
        }
    }
}
