import org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME

plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.serialization")
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


val compileZipline by tasks.creating(JavaExec::class) {
    dependsOn("compileProductionExecutableKotlinJs")
    classpath = compilerConfiguration
    main = "app.cash.zipline.gradle.ZiplineCompilerKt"
    args = listOf(
        "$buildDir/compileSync/main/productionExecutable/kotlin",
        "$buildDir/zipline",
//        "app.cash.zipline.samples.emojisearch.preparePresenters"
    )
}

val jsBrowserProductionRun by tasks.getting {
    dependsOn(compileZipline)
}