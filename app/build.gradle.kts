plugins {
  id("example.android.application")
}

android.namespace = "com.example.project.template"

dependencies {
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