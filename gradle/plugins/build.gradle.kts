plugins {
    `kotlin-dsl`
}

group = "droidkaigi.confsched2022.buildlogic"

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
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
    implementation(libs.moleculeGradlePlugin)
    implementation(libs.kotlinSerializationPlugin)
    implementation(libs.buildkonfigPlugin)
    implementation(libs.firebasePlugin)
    implementation(libs.sqldelightGradlePlugin)
    implementation(libs.detektGradlePlugin)
    implementation(libs.mokoResourcesPlugin)
    implementation(libs.kspGradlePlugin)
    implementation(libs.paparazziGradlePlugin)
    implementation(libs.playOssLicensesPlugin)
}

gradlePlugin {
    plugins {
        // Primitives
        register("androidApplication") {
            id = "droidkaigi.primitive.androidapplication"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidApplicationPlugin"
        }
        register("android") {
            id = "droidkaigi.primitive.android"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidPlugin"
        }
        register("androidKotlin") {
            id = "droidkaigi.primitive.android.kotlin"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidKotlinPlugin"
        }
        register("androidFirebase") {
            id = "droidkaigi.primitive.android.firebase"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidFirebasePlugin"
        }
        register("androidCompose") {
            id = "droidkaigi.primitive.android.compose"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidComposePlugin"
        }
        register("androidHilt") {
            id = "droidkaigi.primitive.android.hilt"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidHiltPlugin"
        }
        register("androidOssLicense") {
            id = "droidkaigi.primitive.android.ossLicense"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidOssLicensePlugin"
        }
        register("dependencyGraph") {
            id = "droidkaigi.primitive.dependencygraph"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.DependencyGraphPlugin"
        }
        register("spotless") {
            id = "droidkaigi.primitive.spotless"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.SpotlessPlugin"
        }
        register("kotlinMpp") {
            id = "droidkaigi.primitive.kmp"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpPlugin"
        }
        register("kotlinMppIos") {
            id = "droidkaigi.primitive.kmp.ios"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpIosPlugin"
        }
        register("kotlinMppAndroid") {
            id = "droidkaigi.primitive.kmp.android"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpAndroidPlugin"
        }
        register("kotlinMppAndroidHilt") {
            id = "droidkaigi.primitive.kmp.android.hilt"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpAndroidHiltPlugin"
        }
        register("kotlinMppJs") {
            id = "droidkaigi.primitive.kmp.js"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpJsPlugin"
        }
        register("kotlinMppKotlinSerialization") {
            id = "droidkaigi.primitive.kmp.serialization"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KotlinSerializationPlugin"
        }
        register("kotlinMppBuildKonfig") {
            id = "droidkaigi.primitive.kmp.buildkonfig"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpBuildKonfigPlugin"
        }
        register("molecule") {
            id = "droidkaigi.primitive.molecule"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.MoleculePlugin"
        }
        register("sqldelight") {
            id = "droidkaigi.primitive.sqldelight"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.SqldelightPlugin"
        }
        register("mokoResources") {
            id = "droidkaigi.primitive.mokoresources"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.MokoResourcesPlugin"
        }
        register("detekt") {
            id = "droidkaigi.primitive.detekt"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.DetektPlugin"
        }
        register("paparazzi") {
            id = "droidkaigi.primitive.android.paparazzi"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.PaparazziPlugin"
        }
        register("showkase") {
            id = "droidkaigi.primitive.android.compose.showkase"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.ShowkasePlugin"
        }
        // Conventions
        register("androidFeature") {
            id = "droidkaigi.convention.androidfeature"
            implementationClass = "io.github.droidkaigi.confsched2022.convention.AndroidFeaturePlugin"
        }
        register("kmp") {
            id = "droidkaigi.convention.kmp"
            implementationClass = "io.github.droidkaigi.confsched2022.convention.KmpPlugin"
        }
    }
}
