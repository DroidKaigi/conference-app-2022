package com.example.project.template.dsl

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("example.dsl")
      }
      extensions.configure<ExampleDsl> {
        androidApplication {
          compose()
          hilt()
        }
      }
    }
  }
}

