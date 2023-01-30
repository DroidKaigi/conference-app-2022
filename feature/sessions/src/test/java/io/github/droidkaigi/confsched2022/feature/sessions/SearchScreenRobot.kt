package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class SearchScreenRobot @Inject constructor() {

    context(RobotTestRule)
    fun typeQuery(query: String) {
        composeTestRule.onNodeWithTag("search").performTextReplacement(query)
    }

    context(RobotTestRule)
    fun checkSessionExists(title: String) {
        composeTestRule.onAllNodes(
            matcher = hasTestTag("session") and hasText(title),
        ).assertAny(hasText(title))
    }

    context(RobotTestRule)
    fun clearInput() {
        composeTestRule.onNodeWithTag("clearInput").performClick()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) SearchScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            SearchRoot(onItemClick = {})
        }
        function(robotTestRule, this@SearchScreenRobot)
    }
}
