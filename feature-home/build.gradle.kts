plugins {
  id("example.dsl")
}

example{
  androidLibrary(namespace = "com.example.project.template.feature.home") {
    compose()
    hilt()
  }
}


dependencies {

  implementation(projects.coreUi)

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