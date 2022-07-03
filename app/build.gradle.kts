plugins {
  id("com.example.primitive.androidapplication")
  id("com.example.primitive.android.compose")
  id("com.example.primitive.android.hilt")
}

android.namespace = "com.example.project.template"

dependencies {
  implementation(projects.featureHome)
  implementation(projects.coreUi)
}