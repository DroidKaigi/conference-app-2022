plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.kmp.serialization")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.data"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.coreModel)
                implementation(libs.ktorClientCore)
                implementation(libs.ktorKotlinxSerialization)
                implementation(libs.ktorContentNegotiation)
                implementation(libs.multiplatformSettingsCoroutines)
                implementation(libs.kotlinxDatetime)
                implementation(libs.multiplatformFirebaseAuth)
                implementation(libs.kermit)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktorClientOkHttp)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.androidxDatastorePreferences)
                implementation(libs.multiplatformSettingsDatastore)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktorClientDarwin)
            }
        }
    }
}