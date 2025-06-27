# Botsi Android SDK Development Guidelines

## Build/Configuration Instructions

### Project Structure
The project consists of three main modules:
- **app**: Example application demonstrating the SDK usage
- **botsi**: Core SDK module containing the business logic
- **botsi-view**: UI components for the SDK

### Build Requirements
- Java 17
- Kotlin 2.1.10
- Android SDK 35 (compile and target)
- Minimum SDK 21

### Building the Project
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build the project using the Gradle build command or Android Studio's build option

```bash
# Build the project from command line
./gradlew build
```

## Testing Information

### Testing Framework
The project uses JUnit 4.13.2 for unit tests and Espresso 3.6.1 for UI tests.

### Running Tests
To run tests, you can use Android Studio's test runner or Gradle commands:

```bash
# Run all unit tests
./gradlew test

# Run all instrumented tests
./gradlew connectedAndroidTest
```

### Adding New Tests
1. Unit tests should be placed in the `src/test/java` directory of the respective module
2. Instrumented tests should be placed in the `src/androidTest/java` directory
3. Follow the existing test patterns and naming conventions

#### Example Unit Test
```kotlin
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
```

Note: In your actual test file, you'll need to include the appropriate package and imports:
```
import org.junit.Test
import org.junit.Assert.*
```

To add test dependencies to a module, include them in the module's build.gradle.kts file:

```kotlin
dependencies {
    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```

## Additional Development Information

### Code Style
- The project follows Kotlin coding conventions
- Use meaningful function and variable names
- Include proper documentation for public APIs
- Follow SOLID principles and clean architecture patterns

### Architecture
- The SDK follows a modular architecture with clear separation of concerns
- Data layer: Contains repositories, data sources, and models
- Domain layer: Contains business logic and use cases
- UI layer: Contains UI components and view models

### Dependency Management
- Dependencies are managed through the `libs.versions.toml` file
- Version catalogs are used to ensure consistent dependency versions across modules

### Publishing
- The SDK is published to GitHub Packages
- Publishing configuration is defined in the buildSrc directory

### Error Handling
- Use proper exception handling and propagate errors to the appropriate layer
- Provide meaningful error messages and logging

### Compose UI
- The project uses Jetpack Compose for UI components
- Follow Compose best practices for performance and maintainability

### Testing Best Practices
- Write tests for all critical functionality
- Use mocks and fakes for dependencies to isolate the code being tested
- Test edge cases and error scenarios
- Keep tests independent and focused on a single functionality
