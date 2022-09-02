// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.molecule")
    id("droidkaigi.primitive.spotless")
    alias(libs.plugins.ksp)
    alias(libs.plugins.paparazzi)
}

android.namespace = "io.github.droidkaigi.confsched2022.template.preview.screenshots"

androidComponents {
    // Disable release builds for this test-only library, no need to run screenshot tests more than
    // once
    beforeVariants(selector().withBuildType("release")) { builder ->
        builder.enable = false
    }
    beforeVariants(selector().withBuildType("prod")) { builder ->
        builder.enable = false
    }
}

dependencies {

    implementation(project(":core-ui"))
    implementation(project(":feature-contributors"))
    implementation(project(":feature-sessions"))

    implementation(libs.composeUi)
    implementation(libs.showkase.runtime)
    ksp(libs.showkase.processor)

    testImplementation(projects.coreTesting)
    testImplementation(libs.testParameterInjector)
}

tasks.named("check") {
    dependsOn("verifyPaparazziDemoDebug")
}

tasks.withType<Test>().configureEach {
    // Increase memory for Paparazzi tests
    maxHeapSize = "2g"
}