package io.github.droidkaigi.confsched2022.feature.about

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class AboutScreenRobot @Inject constructor() {

    context(RobotTestRule)
    fun checkAboutVisible() {
        composeTestRule.onNodeWithText("What is DroidKaigi?")
        composeTestRule.onNodeWithText(
            "DroidKaigiはエンジニアが主役のAndroidカンファレンスです。" +
                "\\nAndroid技術情報の共有とコミュニケーションを目的に、2022年10月5日(水)〜7日(金)の3日間開催します。"
        )
        composeTestRule.onNodeWithContentDescription("Twitter").assertExists()
        composeTestRule.onNodeWithContentDescription("YouTube").assertExists()
        composeTestRule.onNodeWithContentDescription("Medium").assertExists()
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
