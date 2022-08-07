plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.android.hilt")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.data"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.coreModel)
                implementation(libs.ktorClientCore)
            }
        }
    }
}