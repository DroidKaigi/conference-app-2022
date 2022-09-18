import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    id("droidkaigi.convention.kmp")
    id("droidkaigi.primitive.kmp.android.hilt")
    id("droidkaigi.primitive.kmp.serialization")
    id("droidkaigi.primitive.kmp.buildkonfig")
    id("droidkaigi.primitive.sqldelight")
}

android.namespace = "io.github.droidkaigi.confsched2022.core.data"

kotlin {
    explicitApi()

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
                implementation(libs.sqldelightExtensionsCoroutines)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.ktorClientOkHttp)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.androidxDatastorePreferences)
                implementation(libs.multiplatformSettingsDatastore)
                implementation(libs.okHttpLoggingInterceptor)
                implementation(libs.sqldelightDriverAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktorClientDarwin)
                implementation(libs.koin)
                implementation(libs.sqldelightDriverNative)
            }
        }
    }
}

buildkonfig {
    packageName = "io.github.droidkaigi.confsched2022"

    defaultConfigs {
        buildConfigField(STRING, "apiUrl", "https://ssot-api-staging.an.r.appspot.com")
    }
    defaultConfigs("prod") {
        buildConfigField(STRING, "apiUrl", "https://ssot-api.droidkaigi.jp")
    }
}

sqldelight {
    database("Database") {
        packageName = "io.github.droidkaigi.confsched2022.data"
    }
}
