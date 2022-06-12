plugins {
  `kotlin-dsl`
}

group = "com.example.project.template.buildlogic"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
  implementation(libs.androidGradlePlugin)
  implementation(libs.kotlinGradlePlugin)
  implementation(libs.spotlessGradlePlugin)
  implementation(libs.hiltGradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "example.android.application"
      implementationClass = "AndroidApplicationPlugin"
    }
  }
}