package io.github.droidkaigi.confsched2022.data

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(DestructiveMigrationSchema, context, "droidkaigi.db")
    }
}
