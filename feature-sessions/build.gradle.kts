plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.home"

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)
    implementation(projects.coreZipline)
    testImplementation(projects.coreTesting)

    implementation(libs.kermit)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.androidxCoreKtx)
    implementation(libs.composeUi)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.androidxLifecycleLifecycleRuntimeKtx)
    implementation(libs.androidxActivityCompose)
    implementation(libs.accompanistPager)
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}