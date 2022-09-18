package io.github.droidkaigi.confsched2022.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("droidkaigi.primitive.kmp")
                apply("droidkaigi.primitive.kmp.android")
                apply("droidkaigi.primitive.kmp.ios")
                apply("droidkaigi.primitive.spotless")
            }
        }
    }
}
