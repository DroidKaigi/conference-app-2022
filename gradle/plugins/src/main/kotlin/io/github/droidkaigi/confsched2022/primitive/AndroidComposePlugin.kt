package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File

@Suppress("unused")
class AndroidComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            android {
                buildFeatures.compose = true
                composeOptions {
                    kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
                }
                kotlinOptions {
                    freeCompilerArgs = freeCompilerArgs + buildComposeMetricsParameters()
                }
            }
            dependencies {
                // FIXME: Without this, we can not use 1.2 and compile error
                add("kotlinCompilerPluginClasspath", libs.findLibrary("composeCompiler").get())

                implementation(libs.findLibrary("androidxCoreKtx"))
                implementation(libs.findLibrary("composeUi"))
                implementation(libs.findLibrary("composeMaterial3"))
                implementation(libs.findLibrary("composeUiToolingPreview"))
                implementation(libs.findLibrary("androidxLifecycleLifecycleRuntimeKtx"))
                implementation(libs.findLibrary("androidxActivityCompose"))
                testImplementation(libs.findLibrary("junit"))
                androidTestImplementation(libs.findLibrary("androidxTestExtJunit"))
                androidTestImplementation(libs.findLibrary("androidxTestEspressoEspressoCore"))
                androidTestImplementation(libs.findLibrary("composeUiTestJunit4"))
                debugImplementation(libs.findLibrary("composeUiTooling"))
                debugImplementation(libs.findLibrary("composeUiTestManifest"))
            }
        }
    }
}

private fun Project.buildComposeMetricsParameters(): List<String> {
    val metricParameters = mutableListOf<String>()
    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    val enableMetrics = (enableMetricsProvider.orNull == "true")
    if (enableMetrics) {
        val metricsFolder = File(project.buildDir, "compose-metrics")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    val enableReports = (enableReportsProvider.orNull == "true")
    if (enableReports) {
        val reportsFolder = File(project.buildDir, "compose-reports")
        metricParameters.add("-P")
        metricParameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath
        )
    }
    return metricParameters.toList()
}
