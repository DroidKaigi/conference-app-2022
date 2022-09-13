plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.js")
    id("droidkaigi.primitive.kmp.serialization")
    id("droidkaigi.primitive.mokoresources")
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
                api(libs.mokoResources)

                implementation(libs.kotlinSerializationJson)
            }
        }
        val androidMain by getting {
            dependencies{
                api(libs.mokoResourcesCompose)

                implementation(libs.composeRuntime)
            }
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022.model"
    multiplatformResourcesClassName = "Res"
    iosBaseLocalizationRegion = "ja"
}
