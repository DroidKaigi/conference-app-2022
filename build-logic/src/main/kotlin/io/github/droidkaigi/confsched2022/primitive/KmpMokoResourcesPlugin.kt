package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

@Suppress("unused")
class KmpMokoResourcesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dev.icerock.mobile.multiplatform-resources")
            }
            kotlin {
                sourceSets.getByName("commonMain") {
                    dependencies {
                        // allow access to moko resource class
                        api(libs.findLibrary("mokoResources").get())
                    }
                }
            }
            android {
                sourceSets.getByName("main") {
                    // https://github.com/icerockdev/moko-resources/issues/353
                    res.srcDir(listOf(File(buildDir, "generated/moko/androidMain/res")))
                }
            }
        }
    }
}
