package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day.Day1
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import io.github.droidkaigi.confsched2022.model.TimetableItem
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import io.github.droidkaigi.confsched2022.testing.sessions.FakeSessionsRepository
import javax.inject.Inject
import org.amshove.kluent.shouldContain

class SessionScreenRobot @Inject constructor() {
    @Inject lateinit var sessionsRepository: SessionsRepository
    private val fakeSessionsRepository: FakeSessionsRepository
        get() = sessionsRepository as FakeSessionsRepository

    context(RobotTestRule)
    fun checkTimetableVisible() {
        val title = checkNotNull(
            DroidKaigiSchedule.fake()
                .dayToTimetable[Day1]
        )
            .timetableItems[0]
            .title
            .currentLangTitle
        composeTestRule.onNodeWithText(title).assertExists()
    }

    context(RobotTestRule)
    fun checkFavoritedAt(index: Int, isFavorited: Boolean) {
        composeTestRule
            .onFavorite(
                index
            )
            .assert(hasContentDescription("favorite:$isFavorited"))
    }

    context(RobotTestRule)
    fun clickFavoriteAt(index: Int) {
        composeTestRule
            .onFavorite(
                index
            )
            .performClick()
    }

    fun checkFavoriteIsSavedAt(index: Int) {
        val expected = DroidKaigiSchedule.fake().itemAt(index).id
        println(fakeSessionsRepository.savedFavorites)
        fakeSessionsRepository.savedFavorites shouldContain expected
    }

    private fun AndroidComposeTestRule<*, *>.onFavorite(index: Int): SemanticsNodeInteraction {
        val title = DroidKaigiSchedule.fake().itemAt(index)
            .title
            .currentLangTitle

        return onNode(
            matcher = hasTestTag("favorite") and hasAnySibling(hasText(title)),
            useUnmergedTree = true
        )
    }

    private fun DroidKaigiSchedule.itemAt(index: Int): TimetableItem {
        return checkNotNull(
            dayToTimetable[io.github.droidkaigi.confsched2022.model.DroidKaigi2022Day.Day1]
        )
            .timetableItems[index]
    }

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
