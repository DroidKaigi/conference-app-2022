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
    implementation(libs.firebasePlugin)
    implementation(libs.mokoResourcesGenerator)
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
        register("mokoResources") {
            id = "droidkaigi.primitive.kmp.mokoresources"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.KmpMokoResourcesPlugin"
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
        register("molecule") {
            id = "droidkaigi.primitive.molecule"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.MoleculePlugin"
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
