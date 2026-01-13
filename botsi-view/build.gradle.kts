import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.dokka)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.android.compose.plugin)
    alias(libs.plugins.maven.publish)
    `maven-publish`
}

val versionName = BotsiGlobalVars.viewSdkVersion
val botsiArtifactId = BotsiGlobalVars.viewArtifactId
val botsiGroupId = BotsiGlobalVars.groupId

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
    compileOnly(project(":botsi"))
}
