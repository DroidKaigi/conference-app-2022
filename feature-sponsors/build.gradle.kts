plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.sponsors"

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)

    implementation(libs.hiltNavigationCompose)
}
