import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<BaseAppModuleExtension> {
        compileSdk = 32

        defaultConfig {
          minSdk = 23
        }

        compileOptions {
          sourceCompatibility = JavaVersion.VERSION_1_8
          targetCompatibility = JavaVersion.VERSION_1_8
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

          jvmTarget = JavaVersion.VERSION_1_8.toString()
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
          add("coreLibraryDesugaring", libs.findLibrary("androidDesugarJdkLibs").get())
        }

        defaultConfig.targetSdk = 32
      }
    }
  }
}

private fun CommonExtension<*, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
  (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}
