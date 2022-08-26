plugins {
    id("droidkaigi.primitive.androidapplication")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.android.firebase")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022"

android {
    flavorDimensions += "network"
    signingConfigs {
        create("dev") {
            storeFile = project.file("dev.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }
    productFlavors {
        create("dev") {
            signingConfig = signingConfigs.getByName("dev")
            isDefault = true
            applicationIdSuffix = ".dev"
            dimension = "network"
        }
        create("prod") {
            dimension = "network"
        }
    }
    buildTypes {
        debug {
            signingConfig = null
        }
    }
}

dependencies {
    implementation(projects.featureSessions)
    implementation(projects.coreData)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreUi)

    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.androidxNavigationCompose)
    implementation(libs.androidxStartup)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.material3)
}
