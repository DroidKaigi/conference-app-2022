package io.github.droidkaigi.confsched2022.data

import com.squareup.sqldelight.db.SqlDriver

interface DriverFactory {
    fun createDriver(): SqlDriver
}
