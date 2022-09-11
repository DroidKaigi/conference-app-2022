plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.sponsors"

dependencies {
    implementation(projects.coreDesignsystem)

    implementation(libs.hiltNavigationCompose)
}
