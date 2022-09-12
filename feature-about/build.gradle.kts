plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.about"

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)
    testImplementation(projects.coreTesting)

    implementation(libs.accompanistPager)
    implementation(libs.androidxActivityCompose)
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxLifecycleLifecycleRuntimeKtx)
    implementation(libs.coilCompose)
    implementation(libs.composeMaterial3)
    implementation(libs.composeMaterialIcons)
    implementation(libs.composeUi)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.kermit)
    
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}
