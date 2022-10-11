package io.github.droidkaigi.confsched2022.feature.about

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class AboutScreenRobot @Inject constructor() {

    context(RobotTestRule)
    fun checkAboutVisible() {
        composeTestRule.onNodeWithText("What is DroidKaigi?")
        composeTestRule.onNodeWithContentDescription("Twitter").assertExists()
        composeTestRule.onNodeWithContentDescription("YouTube").assertExists()
        composeTestRule.onNodeWithContentDescription("Medium").assertExists()

        // AuxiliaryInformation
        composeTestRule.onNodeWithText("Access")
        composeTestRule.onNodeWithText("Staff List")
        composeTestRule.onNodeWithText("License")
        composeTestRule.onNodeWithText("Code Conduct")
        composeTestRule.onNodeWithText("Privacy Policy")
        composeTestRule.onNodeWithText("App Version")
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) AboutScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            AboutScreenRoot()
        }
        function(robotTestRule, this@AboutScreenRobot)
    }
}
