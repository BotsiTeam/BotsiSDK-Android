# Botsi Android SDK Documentation

The Botsi SDK enables seamless in-app purchases and paywall management in Android applications. This documentation covers the public API methods available for integration.

## Table of Contents
- [Installation](#installation)
- [Initialization](#initialization)
- [Profile Management](#profile-management)
- [Product Management](#product-management)
- [Purchase Operations](#purchase-operations)
- [Paywall Management](#paywall-management)
- [Error Handling](#error-handling)

## Installation

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

### `activate(context, apiKey, customerUserId?, successCallback, errorCallback)`
```kotlin
fun activate(
    context: Context,
    apiKey: String,
    customerUserId: String? = null,
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Activates and initializes the Botsi SDK with your API key. Initialize early in your app lifecycle (e.g., `Application.onCreate()`).

**Parameters:**
- `context`: Application context
- `apiKey`: Your Botsi API key
- `customerUserId`: Optional user identifier to link the session to a specific user
- `successCallback`: Called when initialization succeeds with the user profile
- `errorCallback`: Called when initialization fails

**Example:**
```kotlin
Botsi.activate(
    context = applicationContext,
    apiKey = "your_api_key",
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

### `login(customerUserId, successCallback, errorCallback)`
```kotlin
fun login(
    customerUserId: String,
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Links the SDK session to a specific user in your system. Use this when transitioning from an anonymous session to an authenticated one, typically after user sign-up or login.

**Parameters:**
- `customerUserId`: The unique identifier for the user in your system
- `successCallback`: Called when login succeeds with the user profile
- `errorCallback`: Called when login fails

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

### `logout(successCallback, errorCallback)`
```kotlin
fun logout(
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Ends the current user session and reverts the SDK to an anonymous state. Removes any stored user identifier and clears session-specific data.

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

## Profile Management

### `getProfile(customerUserId, callback)`
```kotlin
fun getProfile(
    customerUserId: String,
    callback: (BotsiProfile) -> Unit
)
```

Retrieves the current user's profile information.

**Parameters:**
- `customerUserId`: The user identifier
- `callback`: Called with the user's profile containing purchase and entitlement information

**Example:**
```kotlin
Botsi.getProfile("user_123") { profile ->
    Log.d("Botsi", "User profile ID: ${profile.profileId}")
    // Access other profile properties
}
```

### `updateProfile(customerUserId, params, successCallback, errorCallback)`
```kotlin
fun updateProfile(
    customerUserId: String,
    params: BotsiUpdateProfileParameters,
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Updates the user's profile with new information.

**Parameters:**
- `customerUserId`: The user identifier
- `params`: Update parameters containing the new profile data
- `successCallback`: Called when update succeeds with the updated profile
- `errorCallback`: Called when update fails

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

## Product Management

### `getProducts(successCallback, errorCallback)`
```kotlin
fun getProducts(
    successCallback: (List<BotsiProduct>) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Retrieves the list of products available for the application.

**Parameters:**
- `successCallback`: Called with an array of product details
- `errorCallback`: Called when fetching products fails

**Example:**
```kotlin
Botsi.getProducts(
    successCallback = { products ->
        Log.d("Botsi", "Available products: ${products.size}")
        products.forEach { product ->
            Log.d("Botsi", "Product: ${product.title} - ${product.price}")
        }
    },
    errorCallback = { error ->
        Log.e("Botsi", "Failed to fetch products: ${error.message}")
    }
)
```

## Purchase Operations

### `makePurchase(activity, product, subscriptionUpdateParams?, isOfferPersonalized, callback, errorCallback)`
```kotlin
fun makePurchase(
    activity: Activity,
    product: BotsiProduct,
    subscriptionUpdateParams: SubscriptionUpdateParams? = null,
    isOfferPersonalized: Boolean = false,
    callback: ((Pair<BotsiProfile, Purchase>)?) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Initiates a purchase for the specified product.

**Parameters:**
- `activity`: The current activity context
- `product`: The product to purchase
- `subscriptionUpdateParams`: Optional parameters for subscription updates
- `isOfferPersonalized`: Whether the offer is personalized
- `callback`: Called when purchase succeeds with updated profile and purchase details
- `errorCallback`: Called when purchase fails

**Example:**
```kotlin
Botsi.makePurchase(
    activity = this,
    product = selectedProduct,
    subscriptionUpdateParams = null,
    isOfferPersonalized = false,
    callback = { result ->
        val (profile, purchase) = result ?: return@makePurchase
        Log.d("Botsi", "Purchase successful! Updated profile: ${profile.profileId}")
        // Handle successful purchase
    },
    errorCallback = { error ->
        Log.e("Botsi", "Purchase failed: ${error.message}")
    }
)
```

### `restoreProducts(successCallback, errorCallback)`
```kotlin
fun restoreProducts(
    successCallback: (BotsiProfile) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Restores previously purchased products for the current user.

**Parameters:**
- `successCallback`: Called when restore succeeds with updated profile
- `errorCallback`: Called when restore fails

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

### `getPaywall(placementId, successCallback, errorCallback)`
```kotlin
fun getPaywall(
    placementId: String,
    successCallback: (BotsiPaywall) -> Unit,
    errorCallback: (Throwable) -> Unit
)
```

Retrieves a paywall configuration for the specified placement ID.

**Parameters:**
- `placementId`: The identifier of the paywall placement
- `successCallback`: Called with the paywall configuration containing UI elements and product references
- `errorCallback`: Called when paywall fetching fails

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

### `logShowPaywall(paywall)`
```kotlin
fun logShowPaywall(paywall: BotsiPaywall)
```

Sends an analytics event when a paywall is shown to the user. Should be called together with `getPaywall()`.

**Parameters:**
- `paywall`: The paywall object obtained from `getPaywall()`

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

## Utility

### `clearCache()`
```kotlin
fun clearCache()
```

Clears the SDK's internal cache. Use this when you need to refresh cached data.

**Example:**
```kotlin
Botsi.clearCache()
Log.d("Botsi", "Cache cleared successfully")
```

## Error Handling

The SDK uses standard Kotlin `Throwable` for error reporting through error callbacks. Common errors include:

- `IllegalStateException("Botsi not activated!")`: SDK hasn't been initialized
- Network errors: Connection issues when communicating with Botsi servers
- Billing errors: Google Play Billing related issues
- Invalid API key: Incorrect or missing API key
- Paywall configuration errors: Issues with paywall setup or fetching

**Best Practices:**
```kotlin
// Always handle errors appropriately
Botsi.makePurchase(
    activity = this,
    product = product,
    callback = { result ->
        // Handle success
    },
    errorCallback = { error ->
        when (error) {
            is IllegalStateException -> {
                // SDK not initialized
                Log.e("Botsi", "SDK not initialized: ${error.message}")
            }
            else -> {
                // Other errors
                Log.e("Botsi", "Purchase error: ${error.message}")
                // Show user-friendly error message
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
