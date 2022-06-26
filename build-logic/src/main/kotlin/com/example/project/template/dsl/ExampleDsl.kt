package com.example.project.template.dsl

import org.gradle.api.provider.Property
import javax.inject.Inject

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import java.util.Optional

abstract class ExampleDsl @Inject constructor(val target: Project) {
  fun androidApplication(
    namespace: String? = null,
    block: AndroidScope.() -> Unit
  ) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<BaseAppModuleExtension> {
        namespace?.let {
          this.namespace = it
        }
        compileSdk = 32

        defaultConfig {
          minSdk = 23
        }

        compileOptions {
          sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
          targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
          isCoreLibraryDesugaringEnabled = true
        }

        kotlinOptions {
          // Treat all Kotlin warnings as errors (disabled by default)
          allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false

          freeCompilerArgs = freeCompilerArgs + listOf(
//              "-opt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
//              "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          )

          jvmTarget = org.gradle.api.JavaVersion.VERSION_1_8.toString()
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
          add("coreLibraryDesugaring", libs.findLibrary("androidDesugarJdkLibs").get())
        }

        defaultConfig.targetSdk = 32
        packagingOptions {
          resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
          }
        }

        block(AndroidScope(target, this))
      }
    }
  }

  fun androidLibrary(
    namespace: String? = null,
    block: AndroidScope.() -> Unit
  ) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<LibraryExtension> {
        namespace?.let {
          this.namespace = it
        }
        compileSdk = 32

        defaultConfig {
          minSdk = 23
        }

        compileOptions {
          sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
          targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_8
          isCoreLibraryDesugaringEnabled = true
        }

        kotlinOptions {
          // Treat all Kotlin warnings as errors (disabled by default)
          allWarningsAsErrors = properties["warningsAsErrors"] as? Boolean ?: false

          freeCompilerArgs = freeCompilerArgs + listOf(
//              "-opt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
//              "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
          )

          jvmTarget = org.gradle.api.JavaVersion.VERSION_1_8.toString()
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
          add("coreLibraryDesugaring", libs.findLibrary("androidDesugarJdkLibs").get())
        }

        defaultConfig.targetSdk = 32
        block(AndroidScope(target, this))
      }
    }
  }
}

class AndroidScope(val target: Project, val androidExtension: TestedExtension) {
  fun compose() {
    with(target) {
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      with(androidExtension) {
        buildFeatures.compose = true
        composeOptions {
          kotlinCompilerExtensionVersion = libs.findVersion("compose").get().toString()
        }
      }
      dependencies {
        implementation(libs.findLibrary("androidxCoreKtx"))
        implementation(libs.findLibrary("composeUi"))
        implementation(libs.findLibrary("composeMaterial"))
        implementation(libs.findLibrary("composeUiToolingPreview"))
        implementation(libs.findLibrary("androidxLifecycleLifecycleRuntimeKtx"))
        implementation(libs.findLibrary("androidxActivityActivityCompose"))
        testImplementation(libs.findLibrary("junit"))
        androidTestImplementation(libs.findLibrary("androidxTestExtJunit"))
        androidTestImplementation(libs.findLibrary("androidxTestEspressoEspressoCore"))
        androidTestImplementation(libs.findLibrary("composeUiTestJunit4"))
        debugImplementation(libs.findLibrary("composeUiTooling"))
        debugImplementation(libs.findLibrary("composeUiTestManifest"))
      }
    }
  }

  fun hilt() {
    with(target) {
      with(pluginManager) {
        apply("kotlin-kapt")
        apply("dagger.hilt.android.plugin")
      }
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      with(androidExtension) {
        packagingOptions{
          resources {
            excludes += "META-INF/gradle/incremental.annotation.processors"
          }
        }
      }
      dependencies {
        implementation(libs.findLibrary("daggerHiltAndroid"))
        kapt(libs.findLibrary("daggerHiltAndroidCompiler"))
      }
    }
  }
}

private fun DependencyHandlerScope.implementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("implementation", artifact.get())
}

private fun DependencyHandlerScope.debugImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("debugImplementation", artifact.get())
}

private fun DependencyHandlerScope.androidTestImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("androidTestImplementation", artifact.get())
}

private fun DependencyHandlerScope.testImplementation(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("testImplementation", artifact.get())
}

private fun DependencyHandlerScope.api(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("api", artifact.get())
}

private fun DependencyHandlerScope.kapt(
  artifact: Optional<Provider<MinimalExternalModuleDependency>>
) {
  add("api", artifact.get())
}

private fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
  (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
