plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.js")
    id("droidkaigi.primitive.kmp.serialization")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.model"

kotlin {
    explicitApiWarning()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.kotlinxCollectionsImmutable)
                api(libs.kotlinxCoroutinesCore)
                api(libs.kotlinxDatetime)
                implementation(libs.kotlinSerializationJson)
            }
        }
        val androidMain by getting {
            dependencies{
                implementation(libs.composeRuntime)
            }
        }
    }
}