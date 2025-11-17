# Botsi Android SDK Documentation

The Botsi SDK enables seamless in-app purchases and paywall management in Android applications using Google Play Billing. This comprehensive SDK provides a complete solution for monetization, user profile management, and analytics tracking.

## Table of Contents
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Build Requirements](#build-requirements)
- [Initialization](#initialization)
- [Profile Management](#profile-management)
- [Purchase Operations](#purchase-operations)
- [Paywall Management](#paywall-management)
- [Botsi-View Module (UI Components)](#botsi-view-module-ui-components)
- [Error Handling](#error-handling)
- [Development Guidelines](#development-guidelines)
- [Architecture](#architecture)

## Installation

<p align="center">
<a href="https://central.sonatype.com/artifact/com.botsi/sdk/versions"><img src="https://img.shields.io/maven-central/v/com.botsi/sdk"></a>
<a href="https://central.sonatype.com/artifact/com.botsi/view/versions"><img src="https://img.shields.io/maven-central/v/com.botsi/view"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.txt"><img src="https://img.shields.io/badge/license-Apache-brightgreen.svg"></a>
</p>

### Maven Central

To integrate the BotsiSDK into your project using Maven Central, add the following dependency to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.botsi:sdk:{version}'
    implementation 'com.botsi:view:{version}'
}
```

3. **Sync your project** and start using the SDK.

## Project Structure

The Botsi Android SDK consists of three main modules:

- **app**: Example application demonstrating the SDK usage and integration patterns
- **botsi**: Core SDK module containing the business logic, API clients, and data management
- **botsi-view**: UI components and Compose-based views for displaying paywalls and purchase flows

This modular architecture allows you to include only the components you need in your application.

## Build Requirements

### Prerequisites
- **Java 17**: Required for compilation and runtime
- **Kotlin 2.1.10**: The SDK is built with Kotlin and requires this version or compatible
- **Android SDK 35**: Compile and target SDK version
- **Minimum SDK 21**: The SDK supports Android API level 21 and above
- **Android Studio**: Latest stable version recommended for development

### Building the Project
If you're contributing to the SDK or building from source:

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Build the project using the Gradle build command or Android Studio's build option

```bash
# Build the project from command line
./gradlew build
```

## Initialization

### `activate(context, apiKey, customerUserId?, clearCache?, successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun activate(
    context: Context,
    apiKey: String,
    customerUserId: String? = null,
    clearCache: Boolean = false,
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Activates and initializes the Botsi SDK with your API key. This method must be called before any other SDK methods can be used. Initialize early in your app lifecycle (e.g., `Application.onCreate()`).

**Parameters:**
- `context`: The Android application context. Should be application context to avoid memory leaks.
- `apiKey`: The API key for authenticating with Botsi services. Obtained from Botsi dashboard.
- `customerUserId`: Optional user identifier to activate the SDK with. If null, an anonymous user will be created.
- `clearCache`: Whether to clear any cached data during activation. Set to true to force fresh data retrieval.
- `successCallback`: Optional callback invoked when activation succeeds, providing the current user profile. Called on the main thread.
- `errorCallback`: Optional callback invoked when activation fails, providing the error details. Called on the main thread.

**Throws:**
- `IllegalArgumentException`: If the API key is empty or invalid

**Example:**
```kotlin
Botsi.activate(
    context = applicationContext,
    apiKey = "your-botsi-api-key",
    clearCache = false,
    successCallback = { profile ->
        // SDK is ready to use
        Log.d("Botsi", "Activated for user: ${profile.customerUserId}")
    },
    errorCallback = { error ->
        // Handle activation failure
        Log.e("Botsi", "Failed to activate SDK", error)
    }
)
```

## Profile Management

### `getProfile(successCallback, errorCallback?)`
```kotlin
@JvmStatic
fun getProfile(
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Retrieves the current user profile from the Botsi service. This method fetches the most up-to-date user profile information including subscription status, purchase history, and custom attributes.

**Parameters:**
- `successCallback`: Callback invoked when the profile is successfully retrieved. Provides the current BotsiProfile. Called on the main thread.
- `errorCallback`: Optional callback invoked when profile retrieval fails. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getProfile(
    successCallback = { profile ->
        Log.d("Botsi", "User ID: ${profile.customerUserId}")
        Log.d("Botsi", "Active subscriptions: ${profile.activeSubscriptions}")
        // Access other profile properties
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to get profile", error)
    }
)
```

### `updateProfile(params?, successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun updateProfile(
    params: BotsiUpdateProfileParameters?,
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Updates the user profile with the provided parameters. This method allows updating various user profile attributes defined in BotsiUpdateProfileParameters. Changes are synchronized with Botsi servers and cached locally for offline access.

**Parameters:**
- `params`: Parameters containing the profile attributes to update. See BotsiUpdateProfileParameters.
- `successCallback`: Callback invoked when the profile is successfully updated. Called on the main thread.
- `errorCallback`: Optional callback invoked when the update fails. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
val updateParams = BotsiUpdateProfileParameters(
    // Set your update parameters here
)

Botsi.updateProfile(
    params = updateParams,
    successCallback = { profile ->
        Log.d("Botsi", "User ID: ${profile.customerUserId}")
        Log.d("Botsi", "Active subscriptions: ${profile.activeSubscriptions}")
        // Access other profile properties
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to update profile: ${error.message}")
    }
)
```

### `identify(customerUserId, successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun identify(
    customerUserId: String,
    successCallback: (() -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Logs in a user with the specified customer ID. This method authenticates a user with the Botsi service and retrieves their profile. If the user doesn't exist, a new profile will be created automatically.

**Parameters:**
- `customerUserId`: The user identifier to log in with.
- `successCallback`: Optional callback invoked when identify succeeds. Called on the main thread.
- `errorCallback`: Optional callback invoked when login fails. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
val currentUserId = "user_12345"
Botsi.identify(
    customerUserId = currentUserId,
    successCallback = {
        Log.d("Botsi", "User identified successfully")
        // The SDK session is updated to identified state
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to identify user: ${error.message}")
    }
)
```

### `logout(successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun logout(
    successCallback: (() -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Logs out the current user from the Botsi service. This method ends the current user session and clears any user-specific data. After logout, the SDK will operate with an anonymous user profile.

**Parameters:**
- `successCallback`: Optional callback invoked when logout succeeds. Called on the main thread.
- `errorCallback`: Optional callback invoked when logout fails. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.logout(
    successCallback = {
        Log.d("Botsi", "User logged out successfully")
        // The SDK session is reverted to an anonymous state
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to logout user: ${error.message}")
    }
)
```

### `setLogLevel(logLevel)`
```kotlin
@JvmStatic
fun setLogLevel(logLevel: BotsiLogLevel)
```

Updates the log level used by the SDK. This method can be called after the SDK has been activated to change the logging verbosity. Useful for debugging during development or reducing log output in production builds.

**Available Log Levels:**
- `VERBOSE`: Most detailed logging, includes all debug information
- `DEBUG`: Debug information useful for development
- `INFO`: General information about SDK operations (default)
- `WARN`: Warning messages about potential issues
- `ERROR`: Only error messages
- `NONE`: Disable all logging

**Parameters:**
- `logLevel`: The new log level to use. See BotsiLogLevel for available options.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
// Set log level to debug for development
Botsi.setLogLevel(BotsiLogLevel.DEBUG)

// Disable logging for production
Botsi.setLogLevel(BotsiLogLevel.NONE)
```

## Purchase Operations


### `makePurchase(activity, product, subscriptionUpdateParams?, callback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun makePurchase(
    activity: Activity,
    product: BotsiProduct,
    subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
    callback: (BotsiProfile, BotsiPurchase) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Initiates a purchase flow for the specified product. This method launches the Google Play Billing purchase flow for the given product and handles the purchase completion, verification, and profile updates. The purchase is automatically validated with Google Play and synchronized with Botsi servers.

**Important Notes:**
- The activity must be in the foreground when calling this method
- The purchase flow is asynchronous and may take several seconds to complete
- Network connectivity is required for purchase verification
- The user's profile will be automatically updated upon successful purchase

**Parameters:**
- `activity`: The activity from which the purchase flow is launched. Must be in foreground.
- `product`: The product to be purchased. Obtained from getPaywallProducts.
- `subscriptionUpdateParams`: Optional parameters for subscription updates or replacements. Used when upgrading, downgrading, or changing subscription plans.
- `callback`: Callback invoked when the purchase is completed successfully. Provides the updated BotsiProfile and BotsiPurchase with purchase details. Called on the main thread.
- `errorCallback`: Optional callback invoked when the purchase fails. Provides error details including billing errors and network issues. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
// For a simple product purchase
Botsi.makePurchase(
    activity = this,
    product = selectedProduct,
    callback = { profile, purchase ->
        Log.d("Botsi", "Purchase successful: ${purchase.productId}")
        Log.d("Botsi", "Updated profile: ${profile.customerUserId}")
    },
    errorCallback = { error ->
        Log.e("Botsi", "Purchase failed", error)
    }
)

// For subscription upgrade/downgrade
val updateParams = BotsiSubscriptionUpdateParameters(
    oldProductId = "old_subscription_id",
    replacementMode = BotsiReplacementMode.WITH_TIME_PRORATION
)
Botsi.makePurchase(
    activity = this,
    product = newSubscriptionProduct,
    subscriptionUpdateParams = updateParams,
    callback = { profile, purchase ->
        Log.d("Botsi", "Subscription updated: ${purchase.productId}")
        Log.d("Botsi", "Updated profile: ${profile.customerUserId}")
    },
    errorCallback = { error ->
        Log.e("Botsi", "Subscription update failed", error)
    }
)
```

### `restorePurchases(successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun restorePurchases(
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Restores previously purchased products for the current user. This method synchronizes the user's purchase history with the Botsi service and updates the user profile with the restored purchases. It queries Google Play for all purchases made by the current Google account and validates them with Botsi servers. This is useful when users reinstall the app or switch devices.

**When to Use:**
- User reinstalled the app and lost their premium features
- User switched to a new device
- User is experiencing issues with subscription recognition
- As part of app startup to ensure purchase state is current

**Parameters:**
- `successCallback`: Callback invoked when products are successfully restored. Provides the updated BotsiProfile with restored purchases. Called on the main thread.
- `errorCallback`: Optional callback invoked when product restoration fails. Provides error details. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.restorePurchases(
    successCallback = { profile ->
        Log.d("Botsi", "Restored purchases for user: ${profile.customerUserId}")
        Log.d("Botsi", "Active subscriptions: ${profile.activeSubscriptions}")
        // Update UI to reflect restored premium features
        updatePremiumUI(profile)
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to restore purchases", error)
        // Show error message to user
    }
)
```

## Paywall Management

### `getPaywall(placementId, successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun getPaywall(
    placementId: String,
    successCallback: (BotsiPaywall) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Retrieves paywall configuration for the specified placement with backend-only data. A paywall represents a monetization screen that can be displayed to users. This method returns only backend parameters without Google Play Store integration. To get products with pricing information from Google Play, use `getPaywallProducts` after retrieving the paywall configuration.

**Parameters:**
- `placementId`: The identifier for the paywall placement
- `successCallback`: Callback invoked when the paywall is successfully retrieved
- `errorCallback`: Optional callback invoked when paywall retrieval fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getPaywall(
    placementId = "premium_upgrade",
    successCallback = { paywall ->
        Log.d("Botsi", "Paywall ID: ${paywall.id}")
        Log.d("Botsi", "Title: ${paywall.title}")
        // Get products with Google Play pricing
        Botsi.getPaywallProducts(paywall) { products ->
            // Display paywall with products
        }

        // Don't forget to log the paywall impression
        Botsi.logShowPaywall(paywall)
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to load paywall", error)
    }
)
```

### `getPaywallProducts(paywall, successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun getPaywallProducts(
    paywall: BotsiPaywall,
    successCallback: (List<BotsiProduct>) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Retrieves products for the specified paywall with Google Play Store pricing data. This method takes a paywall configuration (obtained from getPaywall) and enriches the products with current pricing information from Google Play Store. This is the second step in the paywall loading process.

**Parameters:**
- `paywall`: The paywall configuration obtained from getPaywall.
- `successCallback`: Callback invoked when products are successfully retrieved. Provides a list of BotsiProduct with Google Play pricing. Called on the main thread.
- `errorCallback`: Optional callback invoked when product retrieval fails. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getPaywall(
    placementId = "premium_upgrade",
    successCallback = { paywall ->
        // Get products with Google Play pricing
        Botsi.getPaywallProducts(
            paywall = paywall,
            successCallback = { products ->
                Log.d("Botsi", "Available products: ${products.size}")
                products.forEach { product ->
                    Log.d("Botsi", "Product: ${product.title} - ${product.price}")
                }
                // Display paywall with products
            },
            errorCallback = { error ->
                Log.e("Botsi", "Failed to get paywall products", error)
            }
        )
    }
)
```

### `getPaywallViewConfiguration(paywallId, successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun getPaywallViewConfiguration(
    paywallId: Long,
    successCallback: (JsonElement) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Retrieves the visual configuration for a specific paywall. This method fetches the UI configuration that defines how the paywall should be displayed.

**Parameters:**
- `paywallId`: The unique identifier of the paywall
- `successCallback`: Callback invoked when the configuration is successfully retrieved
- `errorCallback`: Optional callback invoked when configuration retrieval fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getPaywallViewConfiguration(
    paywallId = paywall.id,
    successCallback = { jsonConfig ->
        Log.d("Botsi", "Paywall configuration retrieved")
        // Use the JSON configuration to customize your paywall UI
        parseAndApplyConfiguration(jsonConfig)
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to get paywall configuration: ${error.message}")
    }
)
```

### `logShowPaywall(paywall)`
```kotlin
@JvmStatic
fun logShowPaywall(paywall: BotsiPaywall)
```

Logs an analytics event when a paywall is shown to the user. This method should be called whenever a paywall is displayed to track user interactions and measure paywall performance. The event is sent to Botsi analytics for reporting and optimization purposes.

**Important:** Call this method immediately when the paywall becomes visible to the user to ensure accurate analytics data.

**Parameters:**
- `paywall`: The paywall that was shown to the user

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getPaywall(
    placementId = "main_paywall",
    successCallback = { paywall ->
        // Log the paywall impression for analytics
        Botsi.logShowPaywall(paywall)

        // Display the paywall to the user
        displayPaywall(paywall)
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to get paywall: ${error.message}")
    }
)
```

## Botsi-View Module (UI Components)

The `botsi-view` module provides ready-to-use Jetpack Compose UI components for displaying paywalls and handling purchase flows. This module integrates seamlessly with the core Botsi SDK to provide a complete monetization solution.

### Installation

Add the botsi-view module dependency to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.botsi:view:{version}'
}
```

### BotsiPaywallEntryPoint

The main composable function for displaying paywalls in your Compose UI.

```kotlin
@Composable
fun BotsiPaywallEntryPoint(
    viewConfig: BotsiViewConfig,
    timerResolver: BotsiTimerResolver = BotsiTimerResolver.default,
    eventHandler: BotsiPublicEventHandler? = null,
)
```

**Parameters:**
- `viewConfig`: Configuration containing the paywall and products to display
- `timerResolver`: Optional custom timer resolver for countdown functionality. Uses default 1-hour timer if not provided
- `eventHandler`: Optional event handler for responding to user interactions

### BotsiViewConfig

Configuration class that holds the paywall and products data needed for display.

```kotlin
class BotsiViewConfig(
    val paywall: BotsiPaywall? = null,
    val products: List<BotsiProduct>? = null,
)
```

**Properties:**
- `paywall`: The paywall configuration obtained from `Botsi.getPaywall()`
- `products`: List of products with pricing obtained from `Botsi.getPaywallProducts()`

### BotsiPublicEventHandler

Interface for handling paywall user interactions and events.

```kotlin
interface BotsiPublicEventHandler {
    fun onLoginAction()
    fun onCustomAction(actionId: String)
    fun onSuccessRestore(profile: BotsiProfile)
    fun onErrorRestore(error: Throwable)
    fun onSuccessPurchase(profile: BotsiProfile, purchase: BotsiPurchase)
    fun onErrorPurchase(error: Throwable)
    fun onTimerEnd(actionId: String)
}
```

**Methods:**
- `onLoginAction()`: Called when user taps login/sign-in button
- `onCustomAction(actionId)`: Called when user taps custom action buttons
- `onSuccessRestore(profile)`: Called when purchase restoration succeeds
- `onErrorRestore(error)`: Called when purchase restoration fails
- `onSuccessPurchase(profile, purchase)`: Called when purchase completes successfully
- `onErrorPurchase(error)`: Called when purchase fails
- `onTimerEnd(actionId)`: Called when countdown timer reaches zero

### BotsiTimerResolver

Interface for customizing countdown timer behavior in paywalls.

```kotlin
fun interface BotsiTimerResolver {
    fun timerEndAtDate(timerId: String): Date

    companion object {
        val default: BotsiTimerResolver // 1-hour countdown from current time
    }
}
```

**Methods:**
- `timerEndAtDate(timerId)`: Returns the end date for the specified timer

### Usage Examples

#### Basic Paywall Display

```kotlin
@Composable
fun MyPaywallScreen() {
    var viewConfig by remember { mutableStateOf(BotsiViewConfig()) }

    LaunchedEffect(Unit) {
        // Load paywall data
        Botsi.getPaywall(
            placementId = "premium_upgrade",
            successCallback = { paywall ->
                Botsi.getPaywallProducts(
                    paywall = paywall,
                    successCallback = { products ->
                        viewConfig = BotsiViewConfig(
                            paywall = paywall,
                            products = products
                        )
                    }
                )
            }
        )
    }

    BotsiPaywallEntryPoint(
        viewConfig = viewConfig
    )
}
```

#### Advanced Usage with Event Handling

```kotlin
@Composable
fun AdvancedPaywallScreen() {
    var viewConfig by remember { mutableStateOf(BotsiViewConfig()) }

    val eventHandler = object : BotsiPublicEventHandler {
        override fun onLoginAction() {
            // Handle login button tap
            navigateToLogin()
        }

        override fun onCustomAction(actionId: String) {
            // Handle custom action buttons
            when (actionId) {
                "contact_support" -> openSupportChat()
                "view_terms" -> openTermsOfService()
            }
        }

        override fun onSuccessPurchase(profile: BotsiProfile, purchase: BotsiPurchase) {
            // Handle successful purchase
            showSuccessMessage("Purchase successful!")
            navigateToMainScreen()
        }

        override fun onErrorPurchase(error: Throwable) {
            // Handle purchase error
            showErrorMessage("Purchase failed: ${error.message}")
        }

        override fun onSuccessRestore(profile: BotsiProfile) {
            // Handle successful restore
            showSuccessMessage("Purchases restored!")
            updateUIForPremiumUser(profile)
        }

        override fun onErrorRestore(error: Throwable) {
            // Handle restore error
            showErrorMessage("Restore failed: ${error.message}")
        }

        override fun onTimerEnd(actionId: String) {
            // Handle timer expiration
            when (actionId) {
                "limited_offer" -> hideLimitedTimeOffer()
                "discount_expires" -> removeDiscountPricing()
            }
        }
    }

    // Load paywall data
    LaunchedEffect(Unit) {
        Botsi.getPaywall(
            placementId = "premium_upgrade",
            successCallback = { paywall ->
                Botsi.getPaywallProducts(
                    paywall = paywall,
                    successCallback = { products ->
                        viewConfig = BotsiViewConfig(
                            paywall = paywall,
                            products = products
                        )
                    }
                )
            }
        )
    }

    BotsiPaywallEntryPoint(
        viewConfig = viewConfig,
        eventHandler = eventHandler
    )
}
```

#### Custom Timer Configuration

```kotlin
@Composable
fun PaywallWithCustomTimer() {
    val customTimerResolver = object : BotsiTimerResolver {
        override fun timerEndAtDate(timerId: String): Date {
            return when (timerId) {
                "flash_sale" -> Date(System.currentTimeMillis() + 30 * 60 * 1000L) // 30 minutes
                "weekend_offer" -> getNextMondayMidnight() // Until Monday
                else -> Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000L) // 24 hours
            }
        }
    }

    BotsiPaywallEntryPoint(
        viewConfig = viewConfig,
        timerResolver = customTimerResolver,
        eventHandler = eventHandler
    )
}
```

### Integration with Navigation

```kotlin
@Composable
fun PaywallNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "paywall") {
        composable("paywall") {
            BotsiPaywallEntryPoint(
                viewConfig = viewConfig,
                eventHandler = object : BotsiPublicEventHandler {
                    override fun onLoginAction() {
                        navController.navigate("login")
                    }

                    override fun onSuccessPurchase(profile: BotsiProfile, purchase: BotsiPurchase) {
                        navController.navigate("success") {
                            popUpTo("paywall") { inclusive = true }
                        }
                    }

                    // Implement other methods...
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable("success") {
            PurchaseSuccessScreen()
        }
    }
}
```

### Best Practices

1. **Always load paywall data before displaying**: Ensure you have both paywall configuration and products with pricing before showing the UI.

2. **Handle all event callbacks**: Implement all methods in BotsiPublicEventHandler to provide proper user feedback.

3. **Use proper error handling**: Always handle errors gracefully and provide meaningful feedback to users.

4. **Log paywall impressions**: Call `Botsi.logShowPaywall(paywall)` when the paywall is displayed for analytics.

5. **Manage lifecycle properly**: The composable handles its own lifecycle, but ensure your parent composable doesn't recreate unnecessarily.

6. **Test timer functionality**: If using custom timers, thoroughly test different timer scenarios.

## Error Handling

The SDK uses standard Kotlin `Throwable` for error reporting through error callbacks. The most common error is:

### `IllegalStateException`
This exception is thrown when SDK methods are called before the SDK has been properly activated with the `activate()` method.

```kotlin
// This will throw IllegalStateException if SDK is not activated
try {
    Botsi.getProfile(
        successCallback = { profile ->
            // Handle profile
        },
        errorCallback = { error ->
            Log.e("Botsi", "Failed to get profile", error)
        }
    )
} catch (e: IllegalStateException) {
    Log.e("Botsi", "SDK not activated: ${e.message}")
    // Initialize SDK first
}
```

### Other Common Errors
- Network errors: Connection issues when communicating with Botsi servers
- Billing errors: Google Play Billing related issues during purchases
- Invalid API key: Incorrect or missing API key during activation
- Profile errors: Issues with user profile operations

### Best Practices
```kotlin
// Always activate SDK first
Botsi.activate(
    context = applicationContext,
    apiKey = "your_api_key",
    successCallback = { profile ->
        // SDK is ready, now you can use other methods
        performSDKOperations()
    },
    errorCallback = { error ->
        Log.e("Botsi", "SDK activation failed: ${error.message}")
    }
)

// Handle errors appropriately in all operations
Botsi.makePurchase(
    activity = this,
    product = product,
    callback = { profile, purchase ->
        // Handle successful purchase
        Log.d("Botsi", "Purchase successful: ${purchase.productId}")
    },
    errorCallback = { error ->
        when (error) {
            is IllegalStateException -> {
                Log.e("Botsi", "SDK not initialized: ${error.message}")
                // Show initialization error to user
            }
            else -> {
                Log.e("Botsi", "Purchase error: ${error.message}")
                // Show purchase error to user
            }
        }
    }
)
```

Properly handle these errors in your application to provide appropriate feedback to users and ensure a smooth user experience.

## Development Guidelines

### Code Style
- The project follows Kotlin coding conventions and best practices
- Use meaningful function and variable names that clearly express intent
- Include proper KDoc documentation for all public APIs
- Follow SOLID principles and clean architecture patterns
- Maintain consistent formatting and indentation

### Dependency Management
- Dependencies are managed through the `libs.versions.toml` file using version catalogs
- This ensures consistent dependency versions across all modules
- When adding new dependencies, update the version catalog appropriately

### Publishing
- The SDK is published to GitHub Packages and Maven Central
- Publishing configuration is defined in the buildSrc directory
- Version management follows semantic versioning principles

### Error Handling Best Practices
- Use proper exception handling and propagate errors to the appropriate layer
- Provide meaningful error messages and comprehensive logging
- Handle edge cases gracefully and provide fallback mechanisms
- Always provide error callbacks in public API methods

## Architecture

### Overview
The Botsi SDK follows a clean, modular architecture with clear separation of concerns:

### Layers
- **Data Layer**: Contains repositories, data sources, HTTP clients, and data models
- **Domain Layer**: Contains business logic, use cases, and domain models
- **UI Layer**: Contains UI components, view models, and Compose-based views

### Key Components
- **BotsiFacade**: Main entry point that coordinates all SDK operations
- **DiManager**: Dependency injection manager providing required dependencies
- **Interactors**: Business logic components for profiles, products, and purchases
- **HTTP Clients**: Network communication with Botsi services
- **Analytics Tracker**: Event tracking and analytics reporting

### Design Principles
- **Single Responsibility**: Each class has a single, well-defined purpose
- **Dependency Injection**: All dependencies are injected, making the code testable
- **Thread Safety**: All public methods are thread-safe and callbacks execute on the main thread
- **Error Handling**: Comprehensive error handling with meaningful error messages
- **Caching**: Local caching for offline access and improved performance

### Jetpack Compose Integration
- The SDK uses Jetpack Compose for modern, declarative UI components
- Follow Compose best practices for performance and maintainability
- UI components are reusable and customizable
- State management follows Compose patterns and guidelines

### Threading Model
- All SDK operations are asynchronous and non-blocking
- Network operations are performed on background threads
- Callbacks are always executed on the main thread
- The SDK handles thread management internally

## License

This SDK is released under the [Apache Software License](https://www.apache.org/licenses/LICENSE-2.0.txt).

## Support

Need help? Contact us at [hello@botsi.com](mailto:hello@botsi.com) or visit our [Developer Portal](https://botsi.com) for more tools and documentation.
