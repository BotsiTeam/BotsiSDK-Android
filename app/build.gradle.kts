plugins {
    alias(libs.plugins.android.compose.plugin)
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}

android {
    namespace = "com.botsi.example"

    compileSdk = BotsiGlobalVars.compileSdk

    defaultConfig {
        applicationId = "com.botsi.example"
        minSdk = BotsiGlobalVars.minSdk
        targetSdk = BotsiGlobalVars.targetSdk
        versionCode = 28
        versionName = "1.0"
        multiDexEnabled = true
    }

    buildTypes {
        release {
        }
        debug {
        }
    }

    compileOptions {
        sourceCompatibility = BotsiGlobalVars.javaVersion
        targetCompatibility = BotsiGlobalVars.javaVersion
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = BotsiGlobalVars.jvmTarget
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to "*.jar")))
    implementation(project(":botsi"))
    implementation(project(":botsi-view"))
    implementation(project(":botsi-ai"))
    implementation(platform(libs.kotlin.bom))
    implementation("androidx.multidex:multidex:2.0.1")
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.appcompat.v171)
    implementation(libs.android.billing.client)
    implementation(platform(libs.compose.bom))
    implementation("androidx.compose.material3:material3") {
        exclude(module = "androidx.lifecycle:lifecycle-livedata-core")
    }
    implementation(libs.core.ktx)

    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
}
