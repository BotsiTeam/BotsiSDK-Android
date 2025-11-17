import org.gradle.api.JavaVersion

object BotsiGlobalVars {
    val javaVersion = JavaVersion.VERSION_17
    val jvmTarget = "17"
    val nameSpace = "com.botsi"
    val viewNameSpace = "com.botsi.view"
    val sdkVersion = "1.0.1"
    val viewSdkVersion = "0.0.1-beta"
    val artifactId = "sdk"
    val groupId = "com.botsi"
    val githubMavenUrl = "https://maven.pkg.github.com/BotsiTeam/BotsiSDK-Android"


    val pomName = "Botsi SDK Android"
    val pomDescription = "Botsi SDK Android"
    val pomUrl = "https://github.com/BotsiTeam/BotsiSDK-Android"
    val pomLicenceName = "The Apache Software License, Version 2.0"
    val pomLicenceUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"
    val pomLicenceDistribution = "repo"
    val pomDeveloperId = "swtp-markevych"
    val pomDeveloperName = "Bohdan Markevych"
    val pomDeveloperEmail = "b.markevych@swytapp.com"
    val pomDeveloperOrganization = "Botsi"
    val pomDeveloperOrganizationUrl = "https://botsi.com"
    val pomScmConnection = "scm:git:git://github.com/BotsiTeam/BotsiSDK-Android.git"
    val pomScmDeveloperConnection = "scm:git:ssh://git@github.com/BotsiTeam/BotsiSDK-Android.git"
    val pomScmUrl = "https://github.com/BotsiTeam/BotsiSDK-Android"


    const val compileSdk = 35
    const val targetSdk = 35
    const val minSdk = 21
}