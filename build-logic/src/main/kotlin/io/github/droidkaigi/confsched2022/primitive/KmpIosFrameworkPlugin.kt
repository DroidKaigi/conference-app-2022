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
                val xcf = XCFramework()

                iosX64 {
                    binaries.framework {
                        baseName = "shared"
                        xcf.add(this)
                    }
                }
                iosArm64 {
                    binaries.framework {
                        baseName = "shared"
                        xcf.add(this)
                    }
                }
                iosSimulatorArm64 {
                    binaries.framework {
                        baseName = "shared"
                        xcf.add(this)
                    }
                }
            }
        }
    }
}
