plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    `maven-publish`
}

val versionName = "rc-0.0.1-beta"
val botsiArtifactId = "botsi-sdk"
val botsiGroupId = "com.botsi"
val token = "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"
val swtpUserName = "swtp-markevych"
val mavenUrl = "https://maven.pkg.github.com/BotsiTeam/BotsiSDK-Android"

android {
    compileSdk = BotsiGlobalVars.compileSdk
    lint.targetSdk = BotsiGlobalVars.targetSdk

    defaultConfig {
        minSdk = BotsiGlobalVars.minSdk
        buildConfigField("String", "VERSION_NAME", versionName)
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = BotsiGlobalVars.javaVersion
        targetCompatibility = BotsiGlobalVars.javaVersion
    }

    buildFeatures {
        buildConfig = true
    }

    namespace = "com.botsi"

    kotlinOptions {
        jvmTarget = BotsiGlobalVars.jvmTarget
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    publishing {
        singleVariant("release")
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = botsiGroupId
            artifactId = botsiArtifactId
            version = versionName
            from(components["release"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(mavenUrl)
            credentials {
                username = System.getenv(swtpUserName)
                password = System.getenv(token)
            }
        }
    }
}

dependencies {
    implementation(libs.gson)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.android.billing.client)
    implementation(libs.android.appset)
    implementation(libs.android.ads.identifier)
    implementation(libs.core.ktx)
    implementation(libs.android.lifecycle)
}
