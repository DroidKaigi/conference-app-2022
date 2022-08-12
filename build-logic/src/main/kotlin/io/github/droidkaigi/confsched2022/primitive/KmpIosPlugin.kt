package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("unused")
class KmpIosPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            kotlin {
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                val iosMain = sourceSets.create("iosMain") {
                    dependsOn(sourceSets.getByName("commonMain"))
                }
                sourceSets.getByName("iosX64Main") {
                    dependsOn(iosMain)
                }
                sourceSets.getByName("iosArm64Main") {
                    dependsOn(iosMain)
                }
                sourceSets.getByName("iosSimulatorArm64Main") {
                    dependsOn(iosMain)
                }
                targets.withType<KotlinNativeTarget>().configureEach {
                    binaries.all {
                        binaryOptions["memoryModel"] = "experimental"
                    }
                }
            }
        }
    }
}
