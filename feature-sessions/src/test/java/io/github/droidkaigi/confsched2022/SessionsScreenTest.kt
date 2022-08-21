package io.github.droidkaigi.confsched2022

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SessionsScreenTest {

    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var sessionScreenRobot: SessionScreenRobot

    @Test
    fun testFilterToggle() {
        sessionScreenRobot(robotTestRule) {
            checkFilterIsOff()
            toggleFilter()
            checkFilterIsOn()
        }
    }
}
