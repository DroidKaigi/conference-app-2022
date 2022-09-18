plugins {
    // https://youtrack.jetbrains.com/issue/KTIJ-19369/False-positive-cant-be-called-in-this-context-by-implicit-receiver-with-plugins-in-Gradle-version-catalogs-as-a-TOML-file#focus=Comments-27-6204464.0-0
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradlePlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.androidGradleLibraryPlugin) apply false
    @Suppress("DSL_SCOPE_VIOLATION")
    alias(libs.plugins.kotlinPlugin) apply false
    id("droidkaigi.primitive.dependencygraph")
}
buildscript {
    configurations.all {
        resolutionStrategy.eachDependency {
            when {
                requested.name == "javapoet" -> useVersion("1.13.0")
            }
        }
    }
}

//val modules = listOf(
//    "app-android",
//    "core-zipline",
//    "feature-sessions",
//    "feature-contributors",
//    "feature-about",
//    "feature-map",
//    "feature-announcement",
//    "feature-setting",
//    "feature-staff",
//    "feature-sponsors",
//    "core-ui",
//    "core-designsystem",
//    "core-data",
//    "core-testing",
//    "core-model",
//    "preview-screenshots"
//)
//
//fun String.runCommand(currentWorkingDir: File = file("./")): String {
//    try {
//        val byteOut = java.io.ByteArrayOutputStream()
//        project(":").exec {
//            workingDir = currentWorkingDir
//            commandLine = this@runCommand.split("\\s".toRegex())
//            standardOutput = byteOut
//        }
//        return String(byteOut.toByteArray()).trim()
//    } catch (e: Exception) {
//        return e.message ?: ""
//    }
//}
//
//modules.forEach { filePath ->
//    val (type, name) = filePath.split("-")
//    "rm -Rf $name".runCommand()
//    "mv $filePath $name".runCommand()
//    "mkdir $type".runCommand()
//    "rm -Rf $type/$name".runCommand()
//    "mv $name $type/".runCommand()
//}