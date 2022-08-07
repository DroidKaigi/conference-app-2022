package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("unused")
class KmpIosFrameworkPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            kotlin {
                val xcf = XCFramework("appioscombined")

                iosX64 {
                    binaries.framework {
                        xcf.add(this)
                    }
                }
                iosArm64 {
                    binaries.framework {
                        xcf.add(this)
                    }
                }
                iosSimulatorArm64 {
                    binaries.framework {
                        xcf.add(this)
                    }
                }
            }
        }
    }
}
