import org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME

plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.serialization")
    id("droidkaigi.primitive.kmp.android.hilt")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.zipline"

kotlin {
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.zipline)
                implementation(projects.coreModel)
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.okHttpCore)
                implementation(libs.sqldelightDriverAndroid)
                implementation(libs.ziplineLoader)
            }
        }
    }
}
val compilerConfiguration by configurations.creating {
}

dependencies {
    add(PLUGIN_CLASSPATH_CONFIGURATION_NAME, libs.ziplineKotlinPlugin)
    compilerConfiguration(libs.ziplineGradlePlugin) {
        exclude(module = libs.kotlinGradlePlugin.get().module.name)
    }
}

rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

val compileZipline by tasks.creating(JavaExec::class) {
    dependsOn("compileProductionExecutableKotlinJs")
    classpath = compilerConfiguration
    main = "app.cash.zipline.gradle.ZiplineCompilerKt"
    args = listOf(
        "$buildDir/compileSync/main/productionExecutable/kotlin",
        "$buildDir/zipline",
        "io.github.droidkaigi.confsched2022.presenter.prepareModifiers"
    )
}

val jsBrowserDevelopmentRun by tasks.getting {
    dependsOn(compileZipline)
}