plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.mokoresources")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.resources"

kotlin {
}

multiplatformResources {
    multiplatformResourcesPackage = "io.github.droidkaigi.confsched2022.core.resources"
//    iosBaseLocalizationRegion = "ja" // default "en"
}