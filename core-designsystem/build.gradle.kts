plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.compose")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.designsystem"

kotlin {
    sourceSets {
        val commonMain by getting {
        }
        val androidMain by getting {
            dependencies {
            }
        }
    }
}