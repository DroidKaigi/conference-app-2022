plugins {
    `kotlin-dsl`
}

group = "droidkaigi.confsched2022.buildlogic"

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
    implementation(libs.moleculeGradlePlugin)
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
        register("androidCompose") {
            id = "droidkaigi.primitive.android.compose"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidComposePlugin"
        }
        register("androidHilt") {
            id = "droidkaigi.primitive.android.hilt"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.AndroidHiltPlugin"
        }
        register("spotless") {
            id = "droidkaigi.primitive.spotless"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.SpotlessPlugin"
        }
        register("molecule") {
            id = "droidkaigi.primitive.molecule"
            implementationClass = "io.github.droidkaigi.confsched2022.primitive.MoleculePlugin"
        }

        // Conventions
        register("androidFeature") {
            id = "droidkaigi.convention.androidfeature"
            implementationClass = "io.github.droidkaigi.confsched2022.convention.AndroidFeature"
        }
    }
}