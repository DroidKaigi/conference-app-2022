package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("unused")
class KmpAndroidPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }
            kotlin {
                android()
            }
            android {
                setupAndroid()
                sourceSets {
                    getByName("main") {
                        assets.srcDirs(file("src/androidMain/assets"))
                        manifest.srcFile("src/androidMain/AndroidManifest.xml")
                        java.srcDirs(file("src/androidMain/kotlin"))
                        res.srcDirs(file("src/androidMain/res"))
                    }
                }
            }
        }
    }
}
