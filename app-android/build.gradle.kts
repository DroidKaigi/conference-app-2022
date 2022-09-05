import com.android.build.api.variant.ResValue

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

androidComponents {
    onVariants { variant ->
        val appName = if (variant.name == "prodRelease") {
            "DroidKaigi 2022"
        } else {
            val baseName = "Kaigi22"
            val networkFlavor = variant
                .productFlavors
                .first { it.first == "network" }
            val network = networkFlavor.second
            val buildType = if (variant.buildType == "debug") {
                "-d"
            } else {
                "-r"
            }
            "$baseName$network$buildType"
        }

        variant.resValues.put(
            variant.makeResValueKey("string", "app_name"),
            ResValue(appName)
        )
    }
}

dependencies {
    implementation(projects.featureSessions)
    implementation(projects.featureContributors)
    implementation(projects.featureAbout)
    implementation(projects.coreData)
    implementation(projects.coreDesignsystem)
    implementation(projects.coreUi)
    implementation(projects.coreModel)

    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.androidxNavigationCompose)
    implementation(libs.androidxStartup)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.material3)
    implementation(libs.firebaseCommon)
    implementation(libs.firebaseAuth)
}
