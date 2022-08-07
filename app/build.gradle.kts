plugins {
    id("droidkaigi.primitive.androidapplication")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022"

dependencies {
    implementation(projects.featureSessions)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreUi)

    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.androidxNavigationCompose)
    implementation(libs.hiltNavigationCompose)
}