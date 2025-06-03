plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
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
    }

    namespace = BotsiGlobalVars.nameSpace

    kotlinOptions {
        jvmTarget = BotsiGlobalVars.jvmTarget
    }

    buildTypes {
        release {
        }
    }
}

dependencies {
    if (findProject(":botsi") != null) {
        // Local build - use project dependency
        compileOnly(project(":botsi"))
    } else {
        // External consumer - use JitPack dependency
        implementation("com.github.BotsiTeam:BotsiSDK-Android:${BotsiGlobalVars.sdkVersion}")
    }
}