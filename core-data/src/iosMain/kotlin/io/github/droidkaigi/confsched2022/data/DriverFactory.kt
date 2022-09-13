package io.github.droidkaigi.confsched2022.data

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(DestructiveMigrationSchema, "droidkaigi.db")
    }
}
