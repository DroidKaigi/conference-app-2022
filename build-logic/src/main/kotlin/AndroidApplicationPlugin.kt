import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class AndroidApplicationPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.example {
      androidApplication {
        compose()
        hilt()
      }
    }
  }
}

