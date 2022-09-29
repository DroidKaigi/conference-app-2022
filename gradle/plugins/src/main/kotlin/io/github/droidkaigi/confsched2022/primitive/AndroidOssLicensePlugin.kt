package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused")
class AndroidOssLicensePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.android.gms.oss-licenses-plugin")
            }
            dependencies {
                implementation(libs.findLibrary("playOssLicenses"))
            }
        }
    }
}
