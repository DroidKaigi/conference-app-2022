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
                implementation(projects.coreModel)
                implementation(libs.zipline)
                implementation(libs.kermit)
            }
        }
        val jsMain by getting {
            dependencies {
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(projects.coreUi)
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

val copyJoda by tasks.creating(Copy::class) {
    dependsOn("jsPublicPackageJson")
    dependsOn("compileProductionExecutableKotlinJs")
    from(File(rootProject.buildDir, "js/node_modules/@js-joda/core/dist")) {

    }
    include("**/js-joda.js")
    include("**/js-joda.js.map")
    rename("js-joda.js", "@js-joda.js")
    into("$buildDir/compileSync/main/productionExecutable/kotlin")
}

val adaptJoda by tasks.creating() {
    dependsOn(copyJoda)
    doLast {
        File("$buildDir/compileSync/main/productionExecutable/kotlin").walk()
            .filter {
                it.name == "Kotlin-DateTime-library-kotlinx-datetime-js-ir.js"
            }
            .forEach { it.writeText(it.readText().replace("@js-joda/core", "./@js-joda.js")) }
    }
}

val compileZipline by tasks.creating(JavaExec::class) {
    dependsOn("compileProductionExecutableKotlinJs")
    dependsOn(adaptJoda)
    classpath = compilerConfiguration
    main = "app.cash.zipline.gradle.ZiplineCompilerKt"
    args = listOf(
        "$buildDir/compileSync/main/productionExecutable/kotlin",
        "$buildDir/zipline",
        "io.github.droidkaigi.confsched2022.modifier.prepareModifiers"
    )
}

val jsBrowserDevelopmentRun by tasks.getting {
    dependsOn(compileZipline)
}