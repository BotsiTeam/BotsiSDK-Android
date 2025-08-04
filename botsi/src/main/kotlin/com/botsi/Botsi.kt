package com.botsi

import android.app.Activity
import android.content.Context
import com.botsi.Botsi.activate
import com.botsi.Botsi.getPaywall
import com.botsi.Botsi.getPaywallProducts
import com.botsi.Botsi.getPaywallViewConfiguration
import com.botsi.Botsi.getProfile
import com.botsi.Botsi.identify
import com.botsi.Botsi.logShowPaywall
import com.botsi.Botsi.logout
import com.botsi.Botsi.makePurchase
import com.botsi.Botsi.restorePurchases
import com.botsi.Botsi.setLogLevel
import com.botsi.Botsi.updateProfile
import com.botsi.di.DiManager
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.domain.model.BotsiUpdateProfileParameters
import com.botsi.logging.BotsiLogLevel
import com.google.gson.JsonElement

/**
 * Main entry point for the Botsi SDK.
 *
 * This singleton object provides access to all Botsi SDK functionality including:
 * - SDK activation and initialization
 * - User profile management (create, update, retrieve profiles)
 * - Product and subscription management (retrieve products, pricing information)
 * - Purchase handling (make purchases, restore purchases, subscription updates)
 * - Paywall configuration and display (retrieve paywall data and UI configurations)
 * - Analytics tracking (track paywall shows and user interactions)
 *
 * ## Usage
 * The SDK must be activated with [activate] before any other methods can be used.
 * All methods are thread-safe and can be called from any thread. Callbacks will be
 * executed on the main thread.
 *
 * ## Basic Setup
 * ```kotlin
 * Botsi.activate(
 *     context = applicationContext,
 *     apiKey = "your-api-key",
 *     customerUserId = "user123", // Optional: specify user ID or null for anonymous
 *     successCallback = { profile ->
 *         // SDK activated successfully
 *         Log.d("Botsi", "User profile: ${profile.customerUserId}")
 *     },
 *     errorCallback = { error ->
 *         // Handle activation error
 *         Log.e("Botsi", "Activation failed", error)
 *     }
 * )
 * ```
 *
 * ## Error Handling
 * All methods that can fail provide optional error callbacks. If no error callback
 * is provided, errors will be logged but not propagated to the caller.
 *
 * @since 1.0.0
 */
object Botsi {

    /**
     * The main facade that handles all SDK operations.
     * This is initialized during the [activate] method call.
     */
    private lateinit var facade: BotsiFacade

    /**
     * Dependency injection manager that provides all required dependencies.
     */
    private val diManager = DiManager()

    /**
     * Activates and initializes the Botsi SDK.
     *
     * This method must be called before any other SDK methods can be used.
     * It initializes the dependency injection system, sets up the SDK facade,
     * and establishes connection with Botsi services. The method is idempotent -
     * calling it multiple times will not cause issues.
     *
     * ## Important Notes
     * - This method should be called from the main thread
     * - The context should be the application context to avoid memory leaks
     * - The API key is provided by Botsi and is required for authentication
     * - If a user profile already exists locally, it will be loaded automatically
     *
     * ## Usage Example
     * ```kotlin
     * Botsi.activate(
     *     context = applicationContext,
     *     apiKey = "your-botsi-api-key",
     *     customerUserId = "user123", // Optional: specify user ID or null for anonymous
     *     clearCache = false,
     *     successCallback = { profile ->
     *         // SDK is ready to use
     *         println("Activated for user: ${profile.customerUserId}")
     *     },
     *     errorCallback = { error ->
     *         // Handle activation failure
     *         Log.e("Botsi", "Failed to activate SDK", error)
     *     }
     * )
     * ```
     *
     * @param context The Android application context. Should be application context to avoid memory leaks.
     * @param apiKey The API key for authenticating with Botsi services. Obtained from Botsi dashboard.
     * @param customerUserId Optional user identifier to activate the SDK with. If null, an anonymous user will be created.
     * @param clearCache Whether to clear any cached data during activation. Set to true to force fresh data retrieval.
     * @param successCallback Optional callback invoked when activation succeeds, providing the current user profile.
     *                       Called on the main thread.
     * @param errorCallback Optional callback invoked when activation fails, providing the error details.
     *                     Called on the main thread.
     *
     * @throws IllegalArgumentException if the API key is empty or invalid
     * @see getProfile
     * @see setLogLevel
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun activate(
        context: Context,
        apiKey: String,
        customerUserId: String? = null,
        clearCache: Boolean = false,
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        diManager.initDi(context, apiKey)

        facade = BotsiFacade(
            profileInteractor = diManager.inject(),
            productsInteractor = diManager.inject(),
            purchaseInteractor = diManager.inject(),
            analyticsTracker = diManager.inject(),
        )

        if (clearCache) {
            facade.clearCache()
        }

        facade.activate(
            customerUserId,
            successCallback,
            errorCallback,
        )
    }

    /**
     * Updates the log level used by the SDK.
     *
     * This method can be called after the SDK has been activated to change the logging verbosity.
     * Useful for debugging during development or reducing log output in production builds.
     *
     * ## Available Log Levels
     * - `VERBOSE`: Most detailed logging, includes all debug information
     * - `DEBUG`: Debug information useful for development
     * - `INFO`: General information about SDK operations (default)
     * - `WARN`: Warning messages about potential issues
     * - `ERROR`: Only error messages
     * - `NONE`: Disable all logging
     *
     * @param logLevel The new log level to use. See [BotsiLogLevel] for available options.
     * @throws IllegalStateException if the SDK has not been activated
     * @see activate
     * @since 1.0.0
     */
    @JvmStatic
    fun setLogLevel(logLevel: BotsiLogLevel) {
        checkActivation()
        diManager.updateLogLevel(logLevel)
    }

    /**
     * Retrieves the current user profile from the Botsi service.
     *
     * This method fetches the most up-to-date user profile information including
     * subscription status, purchase history, and custom attributes. The profile
     * is cached locally and updated from the server when this method is called.
     *
     * ## Usage Example
     * ```kotlin
     * Botsi.getProfile(
     *     successCallback = { profile ->
     *         println("User ID: ${profile.customerUserId}")
     *         println("Active subscriptions: ${profile.activeSubscriptions}")
     *         println("Total purchases: ${profile.totalPurchases}")
     *     },
     *     errorCallback = { error ->
     *         Log.e("Botsi", "Failed to get profile", error)
     *     }
     * )
     * ```
     *
     * @param successCallback Callback invoked when the profile is successfully retrieved.
     *                       Provides the current [BotsiProfile]. Called on the main thread.
     * @param errorCallback Optional callback invoked when profile retrieval fails.
     *                     Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see updateProfile
     * @see identify
     * @since 1.0.0
     */
    @JvmStatic
    fun getProfile(
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.getProfile(
            successCallback,
            errorCallback,
        )
    }

    /**
     * Updates the user profile with the provided parameters.
     *
     * This method allows updating various user profile attributes defined in [BotsiUpdateProfileParameters].
     * Changes are synchronized with Botsi servers and cached locally for offline access.
     *
     * @param params Parameters containing the profile attributes to update. See [BotsiUpdateProfileParameters].
     * @param successCallback Callback invoked when the profile is successfully retrieved.
     *                   Provides the current [BotsiProfile]. Called on the main thread.
     * @param errorCallback Optional callback invoked when the update fails. Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getProfile
     * @see BotsiUpdateProfileParameters
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun updateProfile(
        params: BotsiUpdateProfileParameters?,
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.updateProfile(
            params,
            successCallback,
            errorCallback,
        )
    }

    /**
     * Logs in a user with the specified customer ID.
     *
     * This method authenticates a user with the Botsi service and retrieves their profile.
     * If the user doesn't exist, a new profile will be created automatically.
     *
     * @param customerUserId The user identifier to log in with.
     * @param successCallback Optional callback invoked when identify succeeds. Called on the main thread.
     * @param errorCallback Optional callback invoked when login fails. Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getProfile
     * @see logout
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun identify(
        customerUserId: String,
        successCallback: (() -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.identify(
            customerUserId,
            successCallback,
            errorCallback,
        )
    }

    /**
     * Logs out the current user from the Botsi service.
     *
     * This method ends the current user session and clears any user-specific data.
     * After logout, the SDK will operate with an anonymous user profile.
     *
     * @param successCallback Optional callback invoked when logout succeeds. Called on the main thread.
     * @param errorCallback Optional callback invoked when logout fails. Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see identify
     * @see getProfile
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun logout(
        successCallback: (() -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.logout(successCallback, errorCallback)
    }

    /**
     * Restores previously purchased products for the current user.
     *
     * This method synchronizes the user's purchase history with the Botsi service
     * and updates the user profile with the restored purchases. It queries Google Play
     * for all purchases made by the current Google account and validates them with
     * Botsi servers. This is useful when users reinstall the app or switch devices.
     *
     * ## When to Use
     * - User reinstalled the app and lost their premium features
     * - User switched to a new device
     * - User is experiencing issues with subscription recognition
     * - As part of app startup to ensure purchase state is current
     *
     * ## Usage Example
     * ```kotlin
     * Botsi.restorePurchases(
     *     successCallback = { profile ->
     *         println("Restored purchases for user: ${profile.customerUserId}")
     *         println("Active subscriptions: ${profile.activeSubscriptions}")
     *         // Update UI to reflect restored premium features
     *         updatePremiumUI(profile)
     *     },
     *     errorCallback = { error ->
     *         Log.e("Botsi", "Failed to restore purchases", error)
     *         // Show error message to user
     *     }
     * )
     * ```
     *
     * @param successCallback Callback invoked when products are successfully restored.
     *                       Provides the updated [BotsiProfile] with restored purchases.
     *                       Called on the main thread.
     * @param errorCallback Optional callback invoked when product restoration fails.
     *                     Provides error details. Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see makePurchase
     * @see getProfile
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun restorePurchases(
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.restorePurchases(successCallback, errorCallback)
    }

    /**
     * Retrieves paywall configuration for the specified placement with backend-only data.
     *
     * A paywall represents a monetization screen that can be displayed to users.
     * This method returns only backend parameters without Google Play Store integration.
     * To get products with pricing information from Google Play, use [getPaywallProducts]
     * after retrieving the paywall configuration.
     *
     * ## Usage Example
     * ```kotlin
     * Botsi.getPaywall(
     *     placementId = "premium_upgrade",
     *     successCallback = { paywall ->
     *         println("Paywall ID: ${paywall.id}")
     *         println("Title: ${paywall.title}")
     *         // Get products with Google Play pricing
     *         Botsi.getPaywallProducts(paywall) { products ->
     *             // Display paywall with products
     *         }
     *     },
     *     errorCallback = { error ->
     *         Log.e("Botsi", "Failed to load paywall", error)
     *     }
     * )
     * ```
     *
     * @param placementId The identifier for the paywall placement. Configured in Botsi dashboard.
     * @param successCallback Callback invoked when the paywall is successfully retrieved.
     *                       Provides the [BotsiPaywall] configuration. Called on the main thread.
     * @param errorCallback Optional callback invoked when paywall retrieval fails.
     *                     Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getPaywallProducts
     * @see getPaywallViewConfiguration
     * @see logShowPaywall
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun getPaywall(
        placementId: String,
        successCallback: (BotsiPaywall) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.getPaywall(placementId, successCallback, errorCallback)
    }

    /**
     * Retrieves products for the specified paywall with Google Play Store pricing data.
     *
     * This method takes a paywall configuration (obtained from [getPaywall]) and enriches
     * the products with current pricing information from Google Play Store. This is the
     * second step in the paywall loading process.
     *
     * @param paywall The paywall configuration obtained from [getPaywall].
     * @param successCallback Callback invoked when products are successfully retrieved.
     *                       Provides a list of [BotsiProduct] with Google Play pricing.
     *                       Called on the main thread.
     * @param errorCallback Optional callback invoked when product retrieval fails.
     *                     Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getPaywall
     * @see makePurchase
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun getPaywallProducts(
        paywall: BotsiPaywall,
        successCallback: (List<BotsiProduct>) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.getPaywallProducts(paywall, successCallback, errorCallback)
    }

    /**
     * Retrieves the visual configuration for a specific paywall.
     *
     * This method fetches the UI configuration that defines how the paywall should be displayed,
     * including layout, colors, fonts, and other visual elements. The configuration is returned
     * as a JSON object that can be used by UI frameworks to render the paywall.
     *
     * @param paywallId The unique identifier of the paywall. Obtained from [BotsiPaywall.id].
     * @param successCallback Callback invoked when the configuration is successfully retrieved.
     *                       Provides the configuration as a [JsonElement]. Called on the main thread.
     * @param errorCallback Optional callback invoked when configuration retrieval fails.
     *                     Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getPaywall
     * @see getPaywallProducts
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun getPaywallViewConfiguration(
        paywallId: Long,
        successCallback: (JsonElement) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.getPaywallViewConfiguration(paywallId, successCallback, errorCallback)
    }

    /**
     * Initiates a purchase flow for the specified product.
     *
     * This method launches the Google Play Billing purchase flow for the given product
     * and handles the purchase completion, verification, and profile updates. The purchase
     * is automatically validated with Google Play and synchronized with Botsi servers.
     *
     * ## Important Notes
     * - The activity must be in the foreground when calling this method
     * - The purchase flow is asynchronous and may take several seconds to complete
     * - Network connectivity is required for purchase verification
     * - The user's profile will be automatically updated upon successful purchase
     *
     * ## Usage Example
     * ```kotlin
     * // For a simple product purchase
     * Botsi.makePurchase(
     *     activity = this,
     *     product = selectedProduct,
     *     callback = { profile, purchase ->
     *         println("Purchase successful: ${purchase.productId}")
     *         println("Updated profile: ${profile.customerUserId}")
     *     },
     *     errorCallback = { error ->
     *         Log.e("Botsi", "Purchase failed", error)
     *     }
     * )
     *
     * // For subscription upgrade/downgrade
     * val updateParams = BotsiSubscriptionUpdateParameters(
     *     oldProductId = "old_subscription_id",
     *     replacementMode = BotsiReplacementMode.WITH_TIME_PRORATION
     * )
     * Botsi.makePurchase(
     *     activity = this,
     *     product = newSubscriptionProduct,
     *     subscriptionUpdateParams = updateParams,
     *     callback = { profile, purchase -> /* handle success */ },
     *     errorCallback = { error -> /* handle error */ }
     * )
     * ```
     *
     * @param activity The activity from which the purchase flow is launched. Must be in foreground.
     * @param product The product to be purchased. Obtained from [getPaywallProducts].
     * @param subscriptionUpdateParams Optional parameters for subscription updates or replacements.
     *                                Used when upgrading, downgrading, or changing subscription plans.
     * @param callback Callback invoked when the purchase is completed successfully.
     *                Provides the updated [BotsiProfile] and [BotsiPurchase] with purchase details.
     *                Called on the main thread.
     * @param errorCallback Optional callback invoked when the purchase fails.
     *                     Provides error details including billing errors and network issues.
     *                     Called on the main thread.
     * @throws IllegalStateException if the SDK has not been activated
     * @see getPaywallProducts
     * @see restorePurchases
     * @see BotsiSubscriptionUpdateParameters
     * @since 1.0.0
     */
    @JvmStatic
    @JvmOverloads
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
        callback: (BotsiProfile, BotsiPurchase) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.makePurchase(
            activity,
            product,
            subscriptionUpdateParams,
            callback,
            errorCallback
        )
    }

    /**
     * Logs an analytics event when a paywall is shown to the user.
     *
     * This method should be called whenever a paywall is displayed to track user interactions
     * and measure paywall performance. The event is sent to Botsi analytics for reporting
     * and optimization purposes.
     *
     * ## Important
     * Call this method immediately when the paywall becomes visible to the user to ensure
     * accurate analytics data.
     *
     * @param paywall The paywall that was shown to the user. Obtained from [getPaywall].
     * @throws IllegalStateException if the SDK has not been activated
     * @see getPaywall
     * @see makePurchase
     * @since 1.0.0
     */
    @JvmStatic
    fun logShowPaywall(paywall: BotsiPaywall) {
        checkActivation()
        facade.logShowPaywall(paywall)
    }

    /**
     * Verifies that the SDK has been properly activated.
     *
     * This internal method is called before executing any SDK operation to ensure
     * that the SDK has been initialized with the [activate] method. It prevents
     * operations from being performed on an uninitialized SDK instance.
     *
     * @throws IllegalStateException if the SDK has not been activated
     * @see activate
     * @since 1.0.0
     */
    private fun checkActivation() {
        if (::facade.isInitialized.not()) {
            throw IllegalStateException("Botsi not activated!")
        }
    }

}
