plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.molecule")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022.template.core.testing"

dependencies {
    api(projects.coreModel)
    api(projects.coreData)
    api(libs.robolectric)
    api(libs.hiltAndroidTesting)
    api(libs.junit)
    api(libs.junit)
    api(libs.androidxTestExtJunit)
    api(libs.androidxTestEspressoEspressoCore)
}