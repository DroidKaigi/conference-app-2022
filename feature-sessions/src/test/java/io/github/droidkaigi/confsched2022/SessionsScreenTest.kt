package io.github.droidkaigi.confsched2022

import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.droidkaigi.confsched2022.model.SessionsRepository
import javax.inject.Inject
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class SessionsScreenTest {

    @get:Rule val hiltAndroidRule = HiltAndroidWithComposeAutoInjectRule(this)
    @Inject lateinit var sessionsRepository: SessionsRepository

    @Test
    fun addition_isCorrect() {
        println(sessionsRepository)
        assertEquals(4, 2 + 2)
    }
}
