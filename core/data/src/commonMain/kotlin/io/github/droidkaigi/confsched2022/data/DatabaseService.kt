package io.github.droidkaigi.confsched2022.data

public class DatabaseService(
    driverFactory: DriverFactory,
) {
    public val database: Database = Database(driver = driverFactory.createDriver())
}
