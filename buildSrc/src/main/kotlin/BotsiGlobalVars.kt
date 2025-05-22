import org.gradle.api.JavaVersion

object BotsiGlobalVars {
    val javaVersion = JavaVersion.VERSION_17
    val jvmTarget = "17"

    const val compileSdk = 35
    const val targetSdk = 35
    const val minSdk = 21
}