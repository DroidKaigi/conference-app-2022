package io.github.droidkaigi.confsched2022.feature.staff

import androidx.compose.ui.test.onNodeWithText
import io.github.droidkaigi.confsched2022.data.staff.FakeStaffRepository
import io.github.droidkaigi.confsched2022.model.StaffRepository
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import javax.inject.Inject

class StaffScreenRobot @Inject constructor() {
    @Inject lateinit var staffRepository: StaffRepository
    private val fakeStaffRepository: FakeStaffRepository
        get() = staffRepository as FakeStaffRepository

    context(RobotTestRule)
    fun checkStaffVisible() {
        composeTestRule.onNodeWithText(fakeStaffRepository.staff[0].username).assertExists()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule) StaffScreenRobot.() -> Unit
    ) {
        robotTestRule.composeTestRule.setContent {
            StaffScreenRoot()
        }
        function(robotTestRule, this@StaffScreenRobot)
    }
}
