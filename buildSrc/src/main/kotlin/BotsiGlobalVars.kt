import org.gradle.api.JavaVersion

object BotsiGlobalVars {
    val javaVersion = JavaVersion.VERSION_11
    val jvmTarget = "11"

    const val compileSdk = 35
    const val targetSdk = 35
    const val minSdk = 21
}