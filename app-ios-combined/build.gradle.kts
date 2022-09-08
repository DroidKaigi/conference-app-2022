import io.github.droidkaigi.confsched2022.primitive.kotlin
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
        val isCI = System.getenv()["CI"]?.toBoolean() ?: false
        val isCD = System.getenv()["CD"]?.toBoolean() ?: false
        val sourceSets = if (isCD) {
            listOf(iosArm64())
        } else if (isCI) {
            listOf(iosX64())
        } else {
            listOf(iosX64(), iosArm64(), iosSimulatorArm64())
        }

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
                implementation(libs.koin)
            }
        }
    }
}
