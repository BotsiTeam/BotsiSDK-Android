### Botsi Android SDK – Development Guidelines (Project‑Specific)

#### Build and Configuration
- Toolchain
  - Java: 17 (`buildSrc/src/main/kotlin/BotsiGlobalVars.kt` → `javaVersion = JavaVersion.VERSION_17`, `jvmTarget = "17"`)
  - Kotlin: 2.1.10 (`gradle/libs.versions.toml` → `kotlin = "2.1.10"`)
  - Android SDK: compile/target 35, min 21 (see `BotsiGlobalVars.compileSdk/targetSdk/minSdk`)

- Modules
  - `app`: Example application showcasing SDK usage (Compose UI)
  - `botsi`: Core SDK module (business logic, billing, HTTP)
  - `botsi-view`: UI components for the SDK (Compose)
  - `botsi-ai`: AI/assistant UI module

- Version catalogs
  - Dependencies are centralized in `gradle/libs.versions.toml` (Compose BOM, Kotlin BOM, test libs, etc.). Prefer adding dependencies via the catalog to keep versions consistent.

- Building (root)
  - Full build (all modules):
    ```bash
    ./gradlew build
    ```
  - Example app variants:
    ```bash
    ./gradlew :app:assembleDebug
    ./gradlew :app:assembleRelease
    ```
  - SDK module publishing (GitHub Packages) uses `com.vanniktech.maven.publish` with coordinates from `BotsiGlobalVars`. Required `local.properties` entries:
    ```properties
    admin.username=<github-username>
    admin.token=<github-personal-access-token>
    ```
    Repository URL: `https://maven.pkg.github.com/BotsiTeam/BotsiSDK-Android`.

- Compose/AGP
  - Compose is enabled in `app` and `botsi-view`; versions are controlled by the Compose BOM in the catalog. Keep Kotlin and Compose plugin versions aligned with the catalog to avoid ABI/runtime issues.

#### Testing
- Frameworks
  - Unit: JUnit 4.13.2 (`libs.junit`)
  - Instrumented: AndroidX JUnit + Espresso 3.6.1 (`libs.ext.junit`, `libs.espresso.core`). Instrumented tests require a device/emulator and the Android SDK.

- Running tests
  - All unit tests (root):
    ```bash
    ./gradlew test
    ```
  - Unit tests per module (app):
    ```bash
    ./gradlew :app:testDebugUnitTest
    # or
    ./gradlew :app:testReleaseUnitTest
    ```
  - Instrumented tests (app, device/emulator required):
    ```bash
    ./gradlew :app:connectedDebugAndroidTest
    ```

- Adding tests
  - Unit tests go under `MODULE/src/test/java` (or `.../kotlin`) with the module’s package structure.
  - Instrumented tests go under `MODULE/src/androidTest/java`.
  - Ensure module test deps are declared. The `app` module already has:
    ```kotlin
    // app/build.gradle.kts
    dependencies {
        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }
    ```
  - If adding tests to SDK modules, include `testImplementation(libs.junit)` in that module’s `build.gradle.kts`.

- Verified example (executed locally during preparation)
  - Existing unit tests in the app module were executed; all passed.
    - File: `app/src/test/java/com/botsi/example/ExampleUnitTest.kt`
    - To run just this class via Gradle:
      ```bash
      ./gradlew :app:testDebugUnitTest --tests "com.botsi.example.ExampleUnitTest"
      ```

#### Additional Development Notes
- Code style and architecture
  - Kotlin conventions; document public SDK APIs where applicable.
  - Separation of concerns:
    - `botsi`: data/domain, HTTP/billing, models (e.g., `BotsiPaywall`), facades (`Botsi`, `BotsiFacade`).
    - `botsi-view`: Compose UI and mappers (e.g., `BotsiProductItemContentMapper`).
    - `app`: example screens/fragments (e.g., `BotsiAppSetupFragment`, `BotsiViewFragment`) using the SDK.

- Shared configuration via BuildSrc
  - Adjust SDK versions, namespaces, artifact details, and publication metadata in `buildSrc/src/main/kotlin/BotsiGlobalVars.kt`.
  - Bump `sdkVersion`/`viewSdkVersion` there before publishing.

- Publishing notes
  - Modules `botsi` and `botsi-view` are configured with `mavenPublishing` (Sonatype configuration present as well). Ensure credentials exist in `local.properties` before invoking publish tasks.

- Local environment
  - Android SDK 35 must be installed. Set Gradle JDK to 17 in Android Studio. After updating `libs.versions.toml`, sync Gradle to propagate versions.

- Compose/UI specifics
  - Material3 is used in the example app; keep Compose BOM current and aligned with Kotlin (see `compose-bom` and `kotlin` versions in the catalog).

- Error handling & logging
  - Propagate domain-level errors from `botsi` to consumers; add contextual logs around HTTP and billing flows (`botsi/src/main/kotlin/com/botsi/data/http/*`).
