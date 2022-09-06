package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ContributorsScreenTest {

    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var sessionScreenRobot: ContributorsScreenRobot

    @Test
    fun checkContributorsVisible() {
        sessionScreenRobot(robotTestRule) {
            checkContributorsVisible()
        }
    }
}
