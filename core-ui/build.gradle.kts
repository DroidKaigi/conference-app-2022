plugins {
  id("example.dsl")
}

example {
  androidLibrary(namespace = "com.example.project.template.core.ui") {
    compose()
    hilt()
  }
}

//android.namespace = "com.example.project.template.core.ui"

dependencies {
}