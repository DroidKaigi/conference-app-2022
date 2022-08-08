pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
dependencyResolutionManagement {
    // FIXME: When you add this, "Build was configured to prefer settings repositories over project repositories but repository 'ivy' was added" will occur
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}
rootProject.name = "DroidKaigi2022"
include(
    ":app-android",
    ":core-zipline",
    ":appioscombined",
    ":feature-sessions",
    ":core-ui",
    ":core-designsystem",
    ":core-data",
    ":core-model"
)

// for iOS framework name
project(":appioscombined").projectDir = file("app-ios-combined")
