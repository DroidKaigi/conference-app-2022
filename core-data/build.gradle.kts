plugins {
    id("droidkaigi.convention.kmp")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.model"

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