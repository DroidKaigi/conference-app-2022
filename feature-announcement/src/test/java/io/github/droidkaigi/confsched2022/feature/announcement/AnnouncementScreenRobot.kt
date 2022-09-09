package io.github.droidkaigi.confsched2022.feature.announcement

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.feature.announcement.R.string
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class AnnouncementScreenRobot @Inject constructor() {

    context(RobotTestRule)
    fun checkAnnouncementTitleVisible() {
        // TODO: Language-specific testing
        val appBarTitleText = composeTestRule.activity
            .getString(string.announcement_top_app_bar_title)
        composeTestRule.onNodeWithText(appBarTitleText).assertIsDisplayed()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) AnnouncementScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            AnnouncementScreenRoot(
                onNavigationIconClick = {}
            )
        }
        function(robotTestRule, this@AnnouncementScreenRobot)
    }
}
