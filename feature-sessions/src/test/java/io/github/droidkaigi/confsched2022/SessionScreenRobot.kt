package io.github.droidkaigi.confsched2022

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched2022.feature.sessions.SessionsScreenRoot
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class SessionScreenRobot @Inject constructor() {
    context(RobotTestRule)
    fun checkFilterIsOff() {
        composeTestRule.onNodeWithText("Filter is OFF").assertExists()
    }

    context(RobotTestRule)
    fun checkFilterIsOn() {
        composeTestRule.onNodeWithText("Filter is ON").performClick()
    }

    context(RobotTestRule)
    fun toggleFilter() {
        composeTestRule.onNodeWithText("Filter is OFF").performClick()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) SessionScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            SessionsScreenRoot()
        }
        function(robotTestRule, this@SessionScreenRobot)
    }
}
