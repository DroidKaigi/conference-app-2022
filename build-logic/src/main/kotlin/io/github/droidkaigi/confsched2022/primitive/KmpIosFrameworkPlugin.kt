package io.github.droidkaigi.confsched2022.primitive

import com.android.build.gradle.internal.scope.ProjectInfo.Companion.getBaseName
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
                val xcFrameworkName = "appioscombined"
                val xcf = XCFramework(xcFrameworkName)
                val sourceSets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
                sourceSets.forEach {
                    it.binaries.framework {
                        baseName = xcFrameworkName
                        isStatic = true
                        xcf.add(this)
                    }
                }
            }
        }
    }
}
