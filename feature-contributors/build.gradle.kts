// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("droidkaigi.convention.androidfeature")
    alias(libs.plugins.ksp)
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.contributors"

dependencies {
    implementation(projects.coreUi)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreModel)
    testImplementation(projects.coreTesting)

    implementation(libs.kermit)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.androidxCoreKtx)
    implementation(libs.composeUi)
    implementation(libs.composeMaterial3)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.androidxLifecycleLifecycleRuntimeKtx)
    implementation(libs.androidXChromeCustomTabs)
    implementation(libs.androidxActivityCompose)
    implementation(libs.accompanistPager)
    implementation(libs.coilCompose)
    androidTestImplementation(libs.composeUiTestJunit4)
    debugImplementation(libs.composeUiTooling)
    debugImplementation(libs.composeUiTestManifest)

    implementation(libs.showkase.runtime)
    ksp(libs.showkase.processor)
}