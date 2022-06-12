import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.TestedExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun Project.example(block: ProjectScope.() -> Unit) {
  block(ProjectScope(this))
}

class ProjectScope(val target: Project) {
  fun androidApplication(block: AndroidScope.() -> Unit) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("kotlin-kapt")
      }

      extensions.configure<BaseAppModuleExtension> {
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

  fun libraryApplication(block: AndroidScope.() -> Unit) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("kotlin-kapt")
      }

      extensions.configure<LibraryExtension> {
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
      with(androidExtension) {
        buildFeatures.compose = true
        composeOptions {
          val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
          kotlinCompilerExtensionVersion = libs.findVersion("compose").get().toString()
        }
      }
    }
  }

  fun hilt() {
    with(target) {
      with(pluginManager) {
        apply("dagger.hilt.android.plugin")
      }
      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
      dependencies {
        add("implementation", libs.findLibrary("daggerHiltAndroid").get())
        add("kapt", libs.findLibrary("daggerHiltAndroidCompiler").get())
      }
    }
  }
}

private fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
  (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
