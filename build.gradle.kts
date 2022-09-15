plugins {
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradlePlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradleLibraryPlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlinPlugin) apply false
    id("droidkaigi.primitive.dependencygraph")
}
buildscript {
    configurations.all {
        resolutionStrategy.eachDependency {
            when {
                requested.name == "javapoet" -> useVersion("1.13.0")
            }
        }
    }
}
