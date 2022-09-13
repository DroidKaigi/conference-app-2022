package io.github.droidkaigi.confsched2022.data

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

class NativeDriverFactory : DriverFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(DestructiveMigrationSchema, "droidkaigi.db")
    }
}
