plugins {
  id("example.dsl")
}

extensions.configure<com.example.project.template.dsl.ExampleDsl> {
  androidApplication {
    compose()
    hilt()
  }
}

