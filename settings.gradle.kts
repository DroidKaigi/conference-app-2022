pluginManagement {
    includeBuild("gradle/plugins")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
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
        // for datastore-okio
//        maven(url = "https://androidx.dev/snapshots/builds/8938977/artifacts/repository") {
//            content {
//                includeGroup("androidx.datastore")
//            }
//        }
        // for zipline
//        maven("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}
rootProject.name = "DroidKaigi2022"
val modules = listOf(
    "app-android",
    "core-zipline",
    "feature-sessions",
    "feature-contributors",
    "feature-about",
    "feature-map",
    "feature-announcement",
    "feature-setting",
    "feature-staff",
    "feature-sponsors",
    "core-ui",
    "core-designsystem",
    "core-data",
    "core-testing",
    "core-model",
    "preview-screenshots"
)

modules.forEach { filePath ->
    val (type, name) = filePath.split("-")
    val newProjetName = ":$type.$name"
    include(newProjetName)
    project(newProjetName).projectDir = file(filePath)
}

include(":appioscombined")
// for iOS framework name
project(":appioscombined").projectDir = file("app-ios-combined")
