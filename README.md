# Botsi Android SDK

The **Botsi Android SDK** enables seamless integration of in-app purchases, user profile management, and paywall analytics in your Android apps using Google Play Billing.

---

## 🚀 Features

- ✅ Easy SDK activation with optional user linking
- 👤 Retrieve and update user profiles
- 💳 Display dynamic paywalls with real-time product info
- 🔁 Handle subscriptions and one-time purchases
- ♻️ Restore previous purchases
- 📊 Log paywall impressions for analytics

---

## 📦 Installation

Add the SDK dependency in your `build.gradle`:

```groovy
dependencies {
    implementation 'com.botsi:botsi:<latest-version>'
}
```

Replace `<latest-version>` with the latest release from the [Releases](https://github.com/your-org/botsi-android-sdk/releases) page.

---

## 🔧 Initialization

Initialize the SDK early (e.g., in `Application.onCreate()`):

```kotlin
Botsi.activate(
    context = applicationContext,
    apiKey = "your_api_key",
    customerUserId = "user_123", // optional
    successCallback = { profile ->
        Log.d("Botsi", "Initialized for user: ${profile.profileId}")
    },
    errorCallback = { error ->
        Log.e("Botsi", "Init failed: $error")
    }
)
```

---

## 👤 Profile Management

### Get Profile

```kotlin
Botsi.getProfile("user_123") { profile ->
    Log.d("Botsi", "Profile: ${profile.profileId}")
}
```

### Update Profile

```kotlin
val updateParams = BotsiUpdateProfileParameters(...)
Botsi.updateProfile(
    customerUserId = "user_123",
    params = updateParams,
    successCallback = { profile -> /* updated */ },
    errorCallback = { error -> /* handle error */ }
)
```

### Login

```kotlin
Botsi.login(
    customerUserId = "user_123",
    successCallback = { profile -> /* logged in */ },
    errorCallback = { error -> /* handle error */ }
)
```

### Logout

```kotlin
Botsi.logout(
    successCallback = { profile -> /* logged out */ },
    errorCallback = { error -> /* handle error */ }
)
```

---

## 🛒 Product Management

### Get Products

```kotlin
Botsi.getProducts(
    successCallback = { products ->
        products.forEach { Log.d("Product", it.title) }
    },
    errorCallback = { error -> Log.e("Botsi", "Failed: $error") }
)
```

---

## 💰 Purchase Operations

### Make a Purchase

```kotlin
Botsi.makePurchase(
    activity = this,
    product = selectedProduct,
    subscriptionUpdateParams = null,
    isOfferPersonalized = false,
    callback = { result ->
        val (profile, purchase) = result ?: return@makePurchase
        Log.d("Botsi", "Purchased: ${profile.profileId}")
    },
    errorCallback = { error -> Log.e("Botsi", "Purchase failed: $error") }
)
```

### Restore Purchases

```kotlin
Botsi.restoreProducts(
    successCallback = { profile ->
        Log.d("Botsi", "Restored: ${profile.profileId}")
    },
    errorCallback = { error -> Log.e("Botsi", "Restore failed: $error") }
)
```

---

## 💡 Paywall Management

### Get Paywall and Log Event

```kotlin
Botsi.getPaywall(
    placementId = "main_paywall",
    successCallback = { paywall ->
        Botsi.logShowPaywall(paywall)
    },
    errorCallback = { error -> Log.e("Botsi", "Paywall failed: $error") }
)
```

---

## 🧹 Utility

### Clear Cache

```kotlin
Botsi.clearCache()
```

---

## ❗ Error Handling

Most methods include an `errorCallback` with a `Throwable`. Errors may include:

- `IllegalStateException("Botsi not activated!")`
- Network or billing errors
- Incorrect API key or misconfigured paywalls

Be sure to wrap SDK usage with `try-catch` or error callbacks to catch runtime issues.

---

## 🤝 Contributing

We welcome contributions! Please:

- Fork this repo
- Make your changes in a separate branch
- Submit a pull request with a clear description

📢 Found a bug or have a feature request? Submit an issue in [Issues](https://github.com/your-org/botsi-android-sdk/issues)

---

## 📄 License

This SDK is released under the [MIT License](LICENSE).

---

## 📫 Support

Need help? Contact us at [support@botsi.app](mailto:support@botsi.app)  
Or visit our [Developer Portal](https://botsi.app) for more tools and documentation.
