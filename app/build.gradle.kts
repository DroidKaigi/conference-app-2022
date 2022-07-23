plugins {
    id("droidkaigi.primitive.androidapplication")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022.template"

dependencies {
    implementation(projects.featureHome)
    implementation(projects.coreUi)
}