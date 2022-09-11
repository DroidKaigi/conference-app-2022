plugins {
    id("droidkaigi.primitive.android")
    id("droidkaigi.primitive.android.kotlin")
    id("droidkaigi.primitive.android.compose")
    id("droidkaigi.primitive.android.hilt")
    id("droidkaigi.primitive.molecule")
    id("droidkaigi.primitive.spotless")
}

android.namespace = "io.github.droidkaigi.confsched2022.template.core.ui"
android.kotlinOptions.freeCompilerArgs = listOf(
    "-Xexplicit-api=warning",
)

dependencies {
    implementation(libs.accompanistPager)
}