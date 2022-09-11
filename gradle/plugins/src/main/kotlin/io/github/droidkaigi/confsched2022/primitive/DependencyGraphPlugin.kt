package io.github.droidkaigi.confsched2022.primitive

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.register
import java.util.Locale

@Suppress("unused")
class DependencyGraphPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register<DependencyGraphTask>("dependencyGraph")
    }
}

abstract class DependencyGraphTask : DefaultTask() {
    @TaskAction
    fun run() {
        val dot = project.rootProject.buildDir.resolve("reports/dependency-graph/project.dot")
        dot.parentFile.deleteRecursively()
        dot.parentFile.mkdirs()

        dot.appendText(
            """
            |digraph {
            |  graph [label="${project.rootProject.name}\n ",labelloc=t,fontsize=30,ranksep=1.4];
            |  node [style=filled, fillcolor="#bbbbbb"];
            |  rankdir=TB;
            |
        """.trimMargin()
        )

        val rootProjects = mutableListOf<Project>()
        val queue = mutableListOf(project.rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            rootProjects.add(project)
            queue.addAll(project.childProjects.values)
        }

        val projects = LinkedHashSet<Project>()
        val dependencies = LinkedHashMap<Pair<Project, Project>, MutableList<String>>()
        val multiplatformProjects = mutableListOf<Project>()
        val jsProjects = mutableListOf<Project>()
        val androidProjects = mutableListOf<Project>()
        val androidDynamicFeatureProjects = mutableListOf<Project>()
        val javaProjects = mutableListOf<Project>()

        queue.add(project.rootProject)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            queue.addAll(project.childProjects.values)

            if (project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                multiplatformProjects.add(project)
            }
            if (project.plugins.hasPlugin("kotlin2js")) {
                jsProjects.add(project)
            }
            if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("com.android.application")) {
                androidProjects.add(project)
            }
            if (project.plugins.hasPlugin("com.android.dynamic-feature")) {
                androidDynamicFeatureProjects.add(project)
            }
            if (project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                javaProjects.add(project)
            }

            project.configurations.all {
                if (name.toLowerCase(Locale.getDefault()).contains("test")) return@all
                getDependencies()
                    .withType(ProjectDependency::class.java)
                    .map { it.dependencyProject }
                    .forEach { dependency ->
                        projects.add(project)
                        projects.add(dependency)
                        rootProjects.remove(dependency)

                        val graphKey = Pair(project, dependency)
                        val traits = dependencies.computeIfAbsent(graphKey) { mutableListOf() }

                        if (name.toLowerCase(Locale.getDefault()).endsWith("implementation")) {
                            traits.add("style=dotted")
                        }
                    }
            }
        }

        projects.sortedBy { it.path }.also {
            projects.clear()
            projects.addAll(it)
        }

        dot.appendText("\n  # Projects\n\n")
        for (project in projects) {
            val traits = mutableListOf<String>()

            if (rootProjects.contains(project)) {
                traits.add("shape=box")
            }

            if (multiplatformProjects.contains(project)) {
                if (androidProjects.contains(project)) {
                    traits.add("fillcolor=\"#f7ffad\"")
                } else {
                    traits.add("fillcolor=\"#ffd2b3\"")
                }
            } else if (jsProjects.contains(project)) {
                traits.add("fillcolor=\"#ffffba\"")
            } else if (androidProjects.contains(project)) {
                traits.add("fillcolor=\"#baffc9\"")
            } else if (androidDynamicFeatureProjects.contains(project)) {
                traits.add("fillcolor=\"#c9baff\"")
            } else if (javaProjects.contains(project)) {
                traits.add("fillcolor=\"#ffb3ba\"")
            } else {
                traits.add("fillcolor=\"#eeeeee\"")
            }

            dot.appendText("  \"${project.path}\" [${traits.joinToString(", ")}];\n")
        }

        dot.appendText("\n  {rank = same;")
        for (project in projects) {
            if (rootProjects.contains(project)) {
                dot.appendText(" \"${project.path}\";")
            }
        }
        dot.appendText("}\n")

        dot.appendText("\n  # Dependencies\n\n")
        dependencies.forEach { (key, traits) ->
            dot.appendText("  \"${key.first.path}\" -> \"${key.second.path}\"")
            if (traits.isNotEmpty()) {
                dot.appendText(" [${traits.joinToString(", ")}]")
            }
            dot.appendText("\n")
        }

        dot.appendText("}\n")

        project.rootProject.exec {
            commandLine = listOf(
                "dot",
                "-Tpng",
                "-O",
                dot.path
            )
        }
        project.logger.lifecycle("Project module dependency graph created at ${dot.absolutePath}.png")
    }
}
