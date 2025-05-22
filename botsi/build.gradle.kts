import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    `maven-publish`
}

val versionName = "master"
val botsiArtifactId = "botsi"
val botsiGroupId = "com.botsi"
val mavenUrl = "https://maven.pkg.github.com/BotsiTeam/BotsiSDK-Android"

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    compileSdk = BotsiGlobalVars.compileSdk
    lint.targetSdk = BotsiGlobalVars.targetSdk

    defaultConfig {
        minSdk = BotsiGlobalVars.minSdk
        buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
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
            isMinifyEnabled = true
        }
    }
    publishing {
        singleVariant("release")
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = botsiGroupId
                artifactId = botsiArtifactId
                version = versionName
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri(mavenUrl)
                credentials {
                    username = localProperties.getProperty("admin.username")
                    password = localProperties.getProperty("admin.token")
                }
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
