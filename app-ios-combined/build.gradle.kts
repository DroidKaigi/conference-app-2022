import io.github.droidkaigi.confsched2022.primitive.kotlin
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("droidkaigi.primitive.kmp")
    id("droidkaigi.primitive.kmp.android")
    id("droidkaigi.primitive.kmp.ios")
    id("droidkaigi.primitive.mokoresources")
}

kotlin {
    sourceSets {
        val exportProjects = listOf(
            projects.core.model,
            projects.core.designsystem,
            projects.core.data
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
                exportProjects.forEach { project ->
                    export(project)
                }
                export(libs.mokoResources)
            }
        }

        val commonMain by getting {
            dependencies {
                exportProjects.forEach {
                    api(it)
                }
                implementation(libs.koin)
            }
        }
    }
}

android.namespace = "io.github.droidkaigi.confsched2022"

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022"
    multiplatformResourcesClassName = "Res"
    iosBaseLocalizationRegion = "ja"
}
