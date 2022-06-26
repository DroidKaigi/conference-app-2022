plugins {
  `kotlin-dsl`
}

group = "com.example.project.template.buildlogic"

repositories {
  google()
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
  implementation(libs.androidGradlePlugin)
  implementation(libs.kotlinGradlePlugin)
  implementation(libs.spotlessGradlePlugin)
  implementation(libs.hiltGradlePlugin)
}

gradlePlugin {
  plugins {
    register("androidApplication") {
      id = "example.android.application"
      implementationClass = "com.example.project.template.dsl.AndroidApplicationPlugin"
    }
    register("androidFeature") {
      id = "example.android.feature"
      implementationClass = "com.example.project.template.dsl.AndroidFeaturePlugin"
    }
    register("coreUi") {
      id = "example.dsl"
      implementationClass = "com.example.project.template.dsl.CoreUiPlugin"
    }
  }
}