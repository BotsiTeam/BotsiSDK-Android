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
        buildConfigField("String", "VIEW_VERSION_NAME", "\"$versionName\"")
    }

    compileOptions {
        sourceCompatibility = BotsiGlobalVars.javaVersion
        targetCompatibility = BotsiGlobalVars.javaVersion
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    namespace = BotsiGlobalVars.viewNameSpace

    kotlinOptions {
        jvmTarget = BotsiGlobalVars.jvmTarget
    }

    buildTypes {
        release {
        }
    }
}

dependencies {
    implementation(libs.gson)
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.kotlin.bom))
    implementation(libs.coroutines)
    implementation(libs.android.lifecycle)
    implementation(libs.core.ktx)
    implementation(libs.ui.text.google.fonts)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.appcompat.v171)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation("androidx.compose.material3:material3") {
        exclude(module = "androidx.lifecycle:lifecycle-livedata-core")
    }
    if (findProject(":botsi") != null) {
        // Local build - use project dependency
        compileOnly(project(":botsi"))
    } else {
        // External consumer - use JitPack dependency
        implementation("com.github.BotsiTeam:BotsiSDK-Android:${BotsiGlobalVars.sdkVersion}")
    }
}
