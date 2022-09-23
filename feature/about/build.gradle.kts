plugins {
    id("droidkaigi.convention.androidfeature")
}

android.namespace = "io.github.droidkaigi.confsched2022.feature.about"

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    testImplementation(projects.core.testing)

    implementation(libs.accompanistPager)
    implementation(libs.androidxActivityCompose)
    implementation(libs.androidXChromeCustomTabs)
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

    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}
