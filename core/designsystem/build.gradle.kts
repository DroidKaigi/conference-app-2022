plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.detekt")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.designsystem"

kotlin {
    explicitApiWarning()

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
}
