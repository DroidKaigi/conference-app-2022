plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.molecule")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022.template.core.testing"
android.kotlinOptions.freeCompilerArgs = listOf(
    "-Xexplicit-api=warning",
)

dependencies {
    api(projects.core.model)
    api(projects.core.data)
    api(projects.core.zipline)
    api(libs.robolectric)
    api(libs.androidxActivityCompose)
    api(libs.hiltAndroidTesting)
    api(libs.junit)
    api(libs.androidxTestExtJunit)
    api(libs.androidxTestEspressoEspressoCore)
    api(libs.composeUiTestJunit4)
}