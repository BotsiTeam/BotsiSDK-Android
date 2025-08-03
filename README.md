# Botsi Android SDK Documentation

The Botsi SDK enables seamless in-app purchases and paywall management in Android applications using Google Play Billing. This documentation covers the public API methods available for integration.

## Table of Contents
- [Installation](#installation)
- [Initialization](#initialization)
- [Profile Management](#profile-management)
- [Product Management](#product-management)
- [Purchase Operations](#purchase-operations)
- [Paywall Management](#paywall-management)
- [Error Handling](#error-handling)

## Installation

<p align="center">
<a href="https://jitpack.io/#BotsiTeam/BotsiSDK-Android"><img src="https://jitpack.io/v/BotsiTeam/BotsiSDK-Android.svg"></a>
<a href="https://central.sonatype.com/artifact/com.botsi/sdk/versions"><img src="https://img.shields.io/maven-central/v/com.botsi/sdk"></a>
<a href="https://www.apache.org/licenses/LICENSE-2.0.txt"><img src="https://img.shields.io/badge/license-Apache-brightgreen.svg"></a>
</p>

### Maven Central

To integrate the BotsiSDK into your project using Maven Central, add the following dependency to your `build.gradle` file:

```gradle
dependencies {
    implementation 'com.botsi:sdk:{version}'
}
```

### JitPack

Alternatively, you can use JitPack by adding the JitPack repository and dependency:

1. **Add JitPack repository to your root `build.gradle`:**
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. **Add the dependency to your app's `build.gradle`:**
```gradle
dependencies {
    implementation 'com.github.BotsiTeam:BotsiSDK-Android:{version}'
}
```

3. **Sync your project** and start using the SDK.

## Initialization

### `activate(context, apiKey, clearCache?, successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun activate(
    context: Context,
    apiKey: String,
    clearCache: Boolean = false,
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Activates and initializes the Botsi SDK with your API key. This method must be called before any other SDK methods can be used. Initialize early in your app lifecycle (e.g., `Application.onCreate()`).

**Parameters:**
- `context`: The Android application context. Should be application context to avoid memory leaks.
- `apiKey`: The API key for authenticating with Botsi services. Obtained from Botsi dashboard.
- `clearCache`: Whether to clear any cached data during activation. Set to true to force fresh data retrieval.
- `successCallback`: Optional callback invoked when activation succeeds, providing the current user profile. Called on the main thread.
- `errorCallback`: Optional callback invoked when activation fails, providing the error details. Called on the main thread.

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
    successCallback: (BotsiProfile) -> Unit,
    params: BotsiUpdateProfileParameters?,
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

### `identify(customerUserId?, successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun identify(
    customerUserId: String?,
    successCallback: (() -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Logs in a user with the specified customer ID. This method authenticates a user with the Botsi service and retrieves their profile. If the user doesn't exist, a new profile will be created automatically.

**Parameters:**
- `successCallback`: Optional callback invoked when identify succeeds. Called on the main thread.
- `customerUserId`: The user identifier to log in with. Can be null to use anonymous user.
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
    callback: (BotsiPurchase) -> Unit,
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
- `callback`: Callback invoked when the purchase is completed successfully. Provides the BotsiPurchase with purchase details and updated profile. Called on the main thread.
- `errorCallback`: Optional callback invoked when the purchase fails. Provides error details including billing errors and network issues. Called on the main thread.

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.makePurchase(
    activity = this,
    product = selectedProduct,
    callback = { purchase ->
        Log.d("Botsi", "Purchase successful: ${purchase.productId}")
        Log.d("Botsi", "Updated profile: ${purchase.profile}")
    },
    errorCallback = { error ->
        Log.e("Botsi", "Purchase failed", error)
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

Retrieves paywall configuration for the specified placement. A paywall represents a monetization screen that can be displayed to users.

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

Logs an analytics event when a paywall is shown to the user. This method should be called whenever a paywall is displayed to track user interactions.

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
    callback = { purchase ->
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

## License

This SDK is released under the [Apache Software License](https://www.apache.org/licenses/LICENSE-2.0.txt).

## Support

Need help? Contact us at [hello@botsi.com](mailto:hello@botsi.com) or visit our [Developer Portal](https://botsi.com) for more tools and documentation.
