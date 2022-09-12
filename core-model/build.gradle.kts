plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.js")
    id("droidkaigi.primitive.kmp.serialization")
    id("droidkaigi.primitive.mokoresources")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.model"

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

dependencies {
    commonMainApi(libs.mokoResources)
    androidMainApi(libs.mokoResourcesCompose)
}

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022.model"
    multiplatformResourcesClassName = "Res"
    iosBaseLocalizationRegion = "ja"
}
