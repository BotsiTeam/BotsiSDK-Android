plugins {
    id("org.jetbrains.kotlin.plugin.compose") version ("2.0.0")
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.botsi.example"

    compileSdk = BotsiGlobalVars.compileSdk

    defaultConfig {
        applicationId = "com.botsi.example"
        minSdk = BotsiGlobalVars.minSdk
        targetSdk = BotsiGlobalVars.targetSdk
        versionCode = 21
        versionName = "1.0"
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(platform(libs.kotlin.bom))
//    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.0.0"
//    implementation 'androidx.appcompat:appcompat:1.3.0'
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation(libs.android.billing.client)
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.material3:material3") {
        exclude(module = "androidx.lifecycle:lifecycle-livedata-core")
    }
    implementation(libs.core.ktx)

    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
}
