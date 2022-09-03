plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.mokoresources")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.resources"

kotlin {
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

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022.core.resources"
    iosBaseLocalizationRegion = "ja" // default "en"
}