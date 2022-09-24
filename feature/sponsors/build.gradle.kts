plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.sponsors"

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)

    implementation(libs.hiltNavigationCompose)
    implementation(libs.kermit)
    implementation(libs.accompanistFlowlayout)
    implementation(libs.coilCompose)
}
