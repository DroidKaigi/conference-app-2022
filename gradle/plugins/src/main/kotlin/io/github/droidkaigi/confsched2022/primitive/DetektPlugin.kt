package io.github.droidkaigi.confsched2022.primitive

import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.register

@Suppress("unused")
class DetektPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")
            }
            dependencies {
                detektPlugins(libs.findLibrary("twitterComposeRulesDetekt"))
            }
            tasks.register<Detekt>("composeLint") {
                config()
            }
        }
    }

    private fun Detekt.config() {
        parallel = true

        source = project.files("./").asFileTree
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")

        config.setFrom(project.rootDir.resolve("detekt.yml"))

        reports {
            txt.required.set(true)
        }
    }
}
