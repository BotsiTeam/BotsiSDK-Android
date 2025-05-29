import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.maven.publish)
    `maven-publish`
}

val versionName = "0.0.1-beta"
val botsiArtifactId = "sdk"
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

        debug {
        }
    }
}
afterEvaluate {
    publishing {
        repositories {
            mavenCentral()
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

mavenPublishing {
    coordinates(
        groupId = botsiGroupId,
        artifactId = botsiArtifactId,
        version = versionName
    )

    pom {
        name.set("Botsi SDK Android")
        description.set("An Android SDK for payments processing flow from scratch")
        url.set("https://github.com/BotsiTeam/BotsiSDK-Android")

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        // ← **this is required** by Sonatype
        developers {
            developer {
                id.set("swtp-markevych")
                name.set("Bohdan Markevych")
                email.set("b.markevych@swytapp.com")
                organization.set("Botsi")
                organizationUrl.set("https://botsi.com")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/BotsiTeam/BotsiSDK-Android.git")
            developerConnection.set("scm:git:ssh://git@github.com/BotsiTeam/BotsiSDK-Android.git")
            url.set("https://github.com/BotsiTeam/BotsiSDK-Android")
        }
    }

    // publishing to https://central.sonatype.com/
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
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
