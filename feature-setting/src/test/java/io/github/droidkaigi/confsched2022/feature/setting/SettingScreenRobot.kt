package io.github.droidkaigi.confsched2022.feature.setting

import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class SettingScreenRobot @Inject constructor() {

    context (RobotTestRule)
    fun checkSettingVisible() {
        composeTestRule.onNodeWithText("Dark Mode")
        composeTestRule.onNodeWithText("Language")
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) SettingScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            SettingScreenRobot()
        }
        function(robotTestRule, this@SettingScreenRobot)
    }
}