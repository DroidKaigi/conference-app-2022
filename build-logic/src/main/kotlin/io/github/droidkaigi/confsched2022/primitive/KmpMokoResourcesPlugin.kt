package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel.LIFECYCLE
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
                    logger.log(LIFECYCLE, "KmpMokoResourcesPlugin: res.srcDirs:"+res.srcDirs)
                    res.srcDirs(File(buildDir, "generated/moko/androidMain/res"))
                }
            }
        }
    }
}
