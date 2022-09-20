plugins {
    // https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file#focus=Comments-27-6204464.0-0
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradlePlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradleLibraryPlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlinPlugin) apply false
    id("droidkaigi.primitive.dependencygraph")
}
