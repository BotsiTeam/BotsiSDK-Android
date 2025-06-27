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

### `activate(context, apiKey, clearCache?, customerUserId?, successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun activate(
    context: Context,
    apiKey: String,
    clearCache: Boolean = false,
    customerUserId: String? = null,
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Activates and initializes the Botsi SDK with your API key. This method must be called before any other SDK methods can be used. Initialize early in your app lifecycle (e.g., `Application.onCreate()`).

**Parameters:**
- `context`: The Android application context
- `apiKey`: Your Botsi API key for authenticating with Botsi services
- `clearCache`: Whether to clear any cached data during activation (default: false)
- `customerUserId`: Optional user identifier to link the session to a specific user
- `successCallback`: Optional callback invoked when activation succeeds with the user profile
- `errorCallback`: Optional callback invoked when activation fails

**Example:**
```kotlin
Botsi.activate(
    context = applicationContext,
    apiKey = "your_api_key",
    clearCache = false,
    customerUserId = "user_123", // optional
    successCallback = { profile ->
        Log.d("Botsi", "SDK initialized for user: ${profile.profileId}")
        // SDK is now ready for use
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to initialize Botsi SDK: ${error.message}")
    }
)
```

## Profile Management

### `getProfile(customerUserId?, successCallback)`
```kotlin
@JvmStatic
fun getProfile(
    customerUserId: String?,
    successCallback: (BotsiProfile) -> Unit
)
```

Retrieves the user profile from the Botsi service.

**Parameters:**
- `customerUserId`: Optional user identifier. If null, uses the currently logged-in user
- `successCallback`: Callback invoked when the profile is successfully retrieved

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getProfile(
    customerUserId = "user_123", // or null for current user
    successCallback = { profile ->
        Log.d("Botsi", "User profile ID: ${profile.profileId}")
        // Access other profile properties
    }
)
```

### `updateProfile(customerUserId?, params?, successCallback?, errorCallback?)`
```kotlin
@JvmSynthetic
@JvmOverloads
fun updateProfile(
    customerUserId: String?,
    params: BotsiUpdateProfileParameters?,
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Updates the user profile with the provided parameters.

**Parameters:**
- `customerUserId`: Optional user identifier. If null, uses the currently logged-in user
- `params`: Parameters containing the profile attributes to update
- `successCallback`: Optional callback invoked when the profile is successfully updated
- `errorCallback`: Optional callback invoked when the update fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
val updateParams = BotsiUpdateProfileParameters(
    // Set your update parameters here
)

Botsi.updateProfile(
    customerUserId = "user_123",
    params = updateParams,
    successCallback = { profile ->
        Log.d("Botsi", "Profile updated successfully: ${profile.profileId}")
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to update profile: ${error.message}")
    }
)
```

### `login(customerUserId?, successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun login(
    customerUserId: String?,
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Logs in a user with the specified customer ID. This method authenticates a user with the Botsi service and retrieves their profile.

**Parameters:**
- `customerUserId`: The user identifier to log in with
- `successCallback`: Optional callback invoked when login succeeds, providing the user profile
- `errorCallback`: Optional callback invoked when login fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
val currentUserId = "user_12345"
Botsi.login(
    customerUserId = currentUserId,
    successCallback = { profile ->
        Log.d("Botsi", "User logged in successfully: ${profile.profileId}")
        // The SDK session is now linked to the authenticated user
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to login user: ${error.message}")
    }
)
```

### `logout(successCallback?, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun logout(
    successCallback: ((BotsiProfile) -> Unit)? = null,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Logs out the current user from the Botsi service. This method ends the current user session and clears any user-specific data.

**Parameters:**
- `successCallback`: Optional callback invoked when logout succeeds, providing the updated user profile
- `errorCallback`: Optional callback invoked when logout fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.logout(
    successCallback = { profile ->
        Log.d("Botsi", "User logged out successfully")
        // The SDK session is reverted to an anonymous state
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to logout user: ${error.message}")
    }
)
```

## Product Management

### `getProducts(successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun getProducts(
    successCallback: (List<ProductDetails>) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Retrieves available product details from the Google Play Billing Library. This method fetches information about products that can be purchased through the app.

**Parameters:**
- `successCallback`: Callback invoked when products are successfully retrieved
- `errorCallback`: Optional callback invoked when product retrieval fails

**Returns:**
- `List<ProductDetails>`: Google Play Billing ProductDetails objects containing product information

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.getProducts(
    successCallback = { productDetailsList ->
        Log.d("Botsi", "Available products: ${productDetailsList.size}")
        productDetailsList.forEach { productDetails ->
            Log.d("Botsi", "Product: ${productDetails.title}")
            // Access pricing and other product information
        }
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to fetch products: ${error.message}")
    }
)
```

## Purchase Operations

### `makePurchase(activity, product, subscriptionUpdateParams?, isOfferPersonalized, callback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun makePurchase(
    activity: Activity,
    product: BotsiProduct,
    subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
    isOfferPersonalized: Boolean = false,
    callback: ((Pair<BotsiProfile, Purchase?>?) -> Unit),
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Initiates a purchase flow for the specified product. This method launches the Google Play Billing purchase flow and handles purchase completion and verification.

**Parameters:**
- `activity`: The activity from which the purchase flow is launched
- `product`: The BotsiProduct to be purchased
- `subscriptionUpdateParams`: Optional parameters for subscription updates or replacements
- `isOfferPersonalized`: Whether the offer is personalized to the user (for compliance with regulations)
- `callback`: Callback invoked when the purchase is completed, providing the updated profile and purchase details
- `errorCallback`: Optional callback invoked when the purchase fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.makePurchase(
    activity = this,
    product = selectedBotsiProduct,
    subscriptionUpdateParams = null,
    isOfferPersonalized = false,
    callback = { result ->
        val (profile, purchase) = result ?: return@makePurchase
        Log.d("Botsi", "Purchase successful! Updated profile: ${profile.profileId}")
        purchase?.let {
            Log.d("Botsi", "Purchase token: ${it.purchaseToken}")
        }
        // Handle successful purchase
    },
    errorCallback = { error ->
        Log.e("Botsi", "Purchase failed: ${error.message}")
    }
)
```

### `restoreProducts(successCallback, errorCallback?)`
```kotlin
@JvmStatic
@JvmOverloads
fun restoreProducts(
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: ((Throwable) -> Unit)? = null
)
```

Restores previously purchased products for the current user. This method synchronizes the user's purchase history with the Botsi service and updates the user profile with restored purchases.

**Parameters:**
- `successCallback`: Callback invoked when products are successfully restored, providing the updated user profile
- `errorCallback`: Optional callback invoked when product restoration fails

**Throws:**
- `IllegalStateException`: If the SDK has not been activated

**Example:**
```kotlin
Botsi.restoreProducts(
    successCallback = { profile ->
        Log.d("Botsi", "Purchases restored successfully!")
        // Check restored entitlements in profile
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to restore purchases: ${error.message}")
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
    placementId = "main_paywall",
    successCallback = { paywall ->
        Log.d("Botsi", "Paywall retrieved: ${paywall}")
        // Configure your UI with the paywall information
        
        // Don't forget to log the paywall impression
        Botsi.logShowPaywall(paywall)
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to get paywall: ${error.message}")
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
    Botsi.getProfile("user_123") { profile ->
        // Handle profile
    }
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
    isOfferPersonalized = false,
    callback = { result ->
        // Handle success
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
