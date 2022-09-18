package io.github.droidkaigi.confsched2022.data

import com.squareup.sqldelight.db.SqlDriver

public interface DriverFactory {
    public fun createDriver(): SqlDriver
}
