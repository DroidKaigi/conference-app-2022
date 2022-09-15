package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import java.io.File

@Suppress("unused")
class MokoResourcesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("dev.icerock.mobile.multiplatform-resources")

            android {
                sourceSets.getByName("main").apply {
                    assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
                    val os = DefaultNativePlatform.getCurrentOperatingSystem()
                    if (os.isMacOsX) {
                        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
                    }
                }
            }
        }
    }
}
