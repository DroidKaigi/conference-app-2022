// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    id("droidkaigi.convention.androidfeature")
    id("droidkaigi.primitive.android.compose.showkase")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.contributors"

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    testImplementation(projects.core.testing)

    implementation(libs.kermit)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.androidxCoreKtx)
    implementation(libs.composeUi)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.androidxLifecycleLifecycleRuntimeKtx)
    implementation(libs.androidxActivityCompose)
    implementation(libs.accompanistPager)
    implementation(libs.coilCompose)
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}