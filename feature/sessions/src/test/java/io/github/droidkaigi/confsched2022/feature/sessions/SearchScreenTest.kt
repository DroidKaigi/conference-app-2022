package io.github.droidkaigi.confsched2022.feature.sessions

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.model.DroidKaigiSchedule
import io.github.droidkaigi.confsched2022.model.TimetableItem.Session
import io.github.droidkaigi.confsched2022.model.fake
import io.github.droidkaigi.confsched2022.testing.RobotTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SearchScreenTest {

    @get:Rule val robotTestRule = RobotTestRule(this)
    @Inject lateinit var searchScreenRobot: SearchScreenRobot

    @Test
    fun searchWithResult() {
        searchScreenRobot(robotTestRule) {
            DroidKaigiSchedule.fake().dayToTimetable.forEach { (_, timeTable) ->
                timeTable.contents.forEach inner@{
                    if (it.timetableItem !is Session) return@inner
                    val query = it.timetableItem.title.currentLangTitle
                    typeQuery(query)
                    checkSessionExists(query)
                }
            }
        }
    }
}
