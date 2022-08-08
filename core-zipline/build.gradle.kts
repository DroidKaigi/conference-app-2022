import org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.serialization")
}

kotlin {
    jvm()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
//                implementation(libs.zipline)
                implementation(libs.ziplineSnapshot)
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val jvmMain by getting {
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
    compilerConfiguration(libs.ziplineKotlinPlugin)
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

//val jsBrowserProductionRun by tasks.getting {
//    dependsOn(compileZipline)
//}