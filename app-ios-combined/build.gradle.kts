import io.github.droidkaigi.confsched2022.primitive.kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.ios")
}


kotlin {
    sourceSets {
        val projects = listOf(
            projects.coreModel,
            projects.coreDesignsystem,
            projects.coreData
        )
        val xcFrameworkName = "appioscombined"
        val xcf = XCFramework(xcFrameworkName)
        val sourceSets = listOf(iosX64(), iosArm64(), iosSimulatorArm64())
        sourceSets.forEach {
            it.binaries.framework {
                baseName = xcFrameworkName
                isStatic = true
                xcf.add(this)
                projects.forEach { projects ->
                    export(projects)
                }
            }
        }
        val commonMain by getting {
            dependencies {
                projects.forEach {
                    api(it)
                }
            }
        }
    }
}