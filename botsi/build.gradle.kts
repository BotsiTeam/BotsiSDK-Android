import com.vanniktech.maven.publish.SonatypeHost
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.maven.publish)
    `maven-publish`
}

val versionName = BotsiGlobalVars.sdkVersion
val botsiArtifactId = BotsiGlobalVars.artifactId
val botsiGroupId = BotsiGlobalVars.groupId
val mavenUrl = BotsiGlobalVars.githubMavenUrl

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
        name.set(BotsiGlobalVars.pomName)
        description.set(BotsiGlobalVars.pomDescription)
        url.set(BotsiGlobalVars.pomUrl)

        licenses {
            license {
                name.set(BotsiGlobalVars.pomLicenceName)
                url.set(BotsiGlobalVars.pomLicenceUrl)
                distribution.set(BotsiGlobalVars.pomLicenceDistribution)
            }
        }

        // ← **this is required** by Sonatype
        developers {
            developer {
                id.set(BotsiGlobalVars.pomName)
                name.set(BotsiGlobalVars.pomDeveloperName)
                email.set(BotsiGlobalVars.pomDeveloperEmail)
                organization.set(BotsiGlobalVars.pomDeveloperOrganization)
                organizationUrl.set(BotsiGlobalVars.pomDeveloperOrganizationUrl)
            }
        }

        scm {
            connection.set(BotsiGlobalVars.pomScmConnection)
            developerConnection.set(BotsiGlobalVars.pomScmDeveloperConnection)
            url.set(BotsiGlobalVars.pomScmUrl)
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
