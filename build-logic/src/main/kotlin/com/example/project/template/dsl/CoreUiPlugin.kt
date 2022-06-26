package com.example.project.template.dsl

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.create

@Suppress("unused")
class CoreUiPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    val extension = project.extensions.create("example", ExampleDsl::class,
      project)
  }
}

