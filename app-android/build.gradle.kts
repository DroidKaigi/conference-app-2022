import com.android.build.api.variant.ResValue

plugins {
    id("droidkaigi.primitive.androidapplication")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.android.firebase")
    id("droidkaigi.primitive.spotless")
    id("com.google.android.gms.oss-licenses-plugin")
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
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = null
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles("benchmark-rules.pro")
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
    implementation(projects.feature.sessions)
    implementation(projects.feature.contributors)
    implementation(projects.feature.map)
    implementation(projects.feature.announcement)
    implementation(projects.feature.about)
    implementation(projects.feature.setting)
    implementation(projects.feature.staff)
    implementation(projects.feature.sponsors)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
    implementation(projects.core.model)

    implementation(libs.composeMaterialIcons)
    implementation(libs.composeMaterial3WindowSizeClass)
    implementation(libs.androidXChromeCustomTabs)
    implementation(libs.androidxNavigationCompose)
    implementation(libs.androidxStartup)
    implementation(libs.hiltNavigationCompose)
    implementation(libs.material3)
    implementation(libs.firebaseCommon)
    implementation(libs.firebaseAuth)
    implementation(libs.androidxSplashScreen)

    implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")
}
