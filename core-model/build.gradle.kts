plugins {
    id("droidkaigi.convention.kmp")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.model"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            }
        }
    }
}