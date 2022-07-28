package io.github.droidkaigi.confsched2022.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeature : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("droidkaigi.primitive.android")
                apply("droidkaigi.primitive.android.compose")
                apply("droidkaigi.primitive.android.hilt")
                apply("droidkaigi.primitive.spotless")
                apply("droidkaigi.primitive.molecule")
            }
        }
    }
}