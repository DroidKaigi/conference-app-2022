// TODO: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.molecule")
    id("droidkaigi.primitive.spotless")
    id("droidkaigi.primitive.android.compose.showkase")
    id("droidkaigi.primitive.android.paparazzi")
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

    implementation(projects.core.ui)
    implementation(projects.feature.contributors)
    implementation(projects.feature.sessions)

    implementation(libs.composeUi)

    testImplementation(projects.core.testing)
    testImplementation(libs.testParameterInjector)
}

tasks.named("check") {
    dependsOn("verifyPaparazziDemoDebug")
}

tasks.withType<Test>().configureEach {
    // Increase memory for Paparazzi tests
    maxHeapSize = "2g"
}