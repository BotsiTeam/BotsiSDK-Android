plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.android.compose.plugin)
}

val versionName = BotsiGlobalVars.viewSdkVersion

android {
    compileSdk = BotsiGlobalVars.compileSdk
    lint.targetSdk = BotsiGlobalVars.targetSdk

    defaultConfig {
        minSdk = BotsiGlobalVars.minSdk
    }

    compileOptions {
        sourceCompatibility = BotsiGlobalVars.javaVersion
        targetCompatibility = BotsiGlobalVars.javaVersion
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    namespace = "com.botsi.ai"

    kotlinOptions {
        jvmTarget = BotsiGlobalVars.jvmTarget
    }

    buildTypes {
        release {
        }
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.android.appset)
    implementation(libs.android.ads.identifier)
    implementation(libs.android.billing.client)
    implementation(libs.coroutines)
    implementation(libs.android.lifecycle)
    implementation(libs.core.ktx)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.appcompat.v171)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation("androidx.compose.material3:material3")
    implementation(libs.retrofit.converter)
}
