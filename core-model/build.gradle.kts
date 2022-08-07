plugins {
    id("droidkaigi.convention.kmp")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.data"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinxCollectionsImmutable)
                api(libs.kotlinxCoroutinesCore)
            }
        }
    }
}