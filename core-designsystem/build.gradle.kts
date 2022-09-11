plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.mokoresources")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.designsystem"

kotlin {
    sourceSets {
        val commonMain by getting {
        }
        val androidMain by getting {
        }
    }
}

dependencies {
    kotlinCompilerPluginClasspath(libs.composeCompiler)
    implementation(libs.coilCompose)

    commonMainApi(libs.mokoResources)
    androidMainApi(libs.mokoResourcesCompose)
}

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022.designsystem"
    multiplatformResourcesClassName = "MR"
    iosBaseLocalizationRegion = "ja"
}
