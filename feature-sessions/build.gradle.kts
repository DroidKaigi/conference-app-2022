plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.home"

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)
    implementation(projects.coreZipline)

    implementation(libs.hiltNavigationCompose)
    implementation(libs.androidxCoreKtx)
    implementation(libs.composeUi)
    implementation(libs.composeMaterial)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.androidxLifecycleLifecycleRuntimeKtx)
    implementation(libs.androidxActivityActivityCompose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidxTestExtJunit)
    androidTestImplementation(libs.androidxTestEspressoEspressoCore)
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)
}