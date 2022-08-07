plugins {
    id("droidkaigi.convention.kmp")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.model"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinxCollectionsImmutable)
                api(libs.kotlinxCoroutinesCore)
            }
        }
        val androidMain by getting {
            dependencies{
                implementation(libs.composeRuntime)
            }
        }
    }
}