package io.github.droidkaigi.confsched2022.feature.contributors

import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.data.contributors.FakeContributorsRepository
import io.github.droidkaigi.confsched2022.model.Contributor
import io.github.droidkaigi.confsched2022.model.ContributorsRepository
import io.github.droidkaigi.confsched2022.model.fakes
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class ContributorsScreenRobot @Inject constructor() {
    @Inject lateinit var contributorsRepository: ContributorsRepository
    val fakeContributorsReposiotry: FakeContributorsRepository
        get() = contributorsRepository as FakeContributorsRepository

    context(RobotTestRule)
    fun checkContributorsVisible() {
        composeTestRule.onNodeWithText(Contributor.fakes()[0].name).assertExists()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) ContributorsScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            ContributorsScreenRoot()
        }
        function(robotTestRule, this@ContributorsScreenRobot)
    }
}
