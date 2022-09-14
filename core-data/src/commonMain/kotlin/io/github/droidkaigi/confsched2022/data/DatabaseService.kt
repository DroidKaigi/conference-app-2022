package io.github.droidkaigi.confsched2022.data

class DatabaseService(
    driverFactory: DriverFactory,
) {
    val database: Database = Database(driver = driverFactory.createDriver())
}
