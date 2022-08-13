package io.github.droidkaigi.confsched2022

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class HiltAndroidWithComposeAutoInjectRule(private val testInstance: Any) : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        val composeTestRule = createAndroidComposeRule<HiltTestActivity>()
        return RuleChain
            .outerRule(HiltAndroidAutoInjectRule(testInstance))
            .around(composeTestRule)
            .apply(base, description)
    }
}
