package com.botsi

import android.app.Activity
import android.content.Context
import com.botsi.Botsi.activate
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
 * - User profile management
 * - Product and subscription management
 * - Purchase handling
 * - Paywall configuration and display
 *
 * The SDK must be activated with [activate] before any other methods can be used.
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
     * It initializes the dependency injection system and sets up the SDK facade.
     *
     * @param context The Android application context
     * @param apiKey The API key for authenticating with Botsi services
     * @param clearCache Whether to clear any cached data during activation (default: false)
     * @param customerUserId Optional user identifier for the current user
     * @param logLevel The log level to use for SDK logging (default: INFO)
     * @param successCallback Optional callback that is invoked when activation succeeds, providing the user profile
     * @param errorCallback Optional callback that is invoked when activation fails
     */
    @JvmStatic
    @JvmOverloads
    fun activate(
        context: Context,
        apiKey: String,
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
            successCallback,
            errorCallback,
        )
    }

    /**
     * Updates the log level used by the SDK.
     *
     * This method can be called after the SDK has been activated to change the logging verbosity.
     *
     * @param logLevel The new log level to use
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    fun setLogLevel(logLevel: BotsiLogLevel) {
        checkActivation()
        diManager.updateLogLevel(logLevel)
    }

    /**
     * Retrieves the user profile from the Botsi service.
     *
     * @param customerUserId Optional user identifier. If null, uses the currently logged-in user
     * @param successCallback Callback that is invoked when the profile is successfully retrieved
     * @throws IllegalStateException if the SDK has not been activated
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
     *
     * @param customerUserId Optional user identifier. If null, uses the currently logged-in user
     * @param params Parameters containing the profile attributes to update
     * @param successCallback Optional callback that is invoked when the profile is successfully updated
     * @param errorCallback Optional callback that is invoked when the update fails
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    @JvmOverloads
    fun updateProfile(
        params: BotsiUpdateProfileParameters?,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.updateProfile(
            params,
            errorCallback
        )
    }

    /**
     * Logs in a user with the specified customer ID.
     *
     * This method authenticates a user with the Botsi service and retrieves their profile.
     *
     * @param customerUserId The user identifier to log in with
     * @param errorCallback Optional callback that is invoked when login fails
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    @JvmOverloads
    fun identify(
        customerUserId: String?,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.identify(
            customerUserId,
            errorCallback
        )
    }

    /**
     * Logs out the current user from the Botsi service.
     *
     * This method ends the current user session and clears any user-specific data.
     *
     * @param successCallback Optional callback that is invoked when logout succeeds, providing the updated user profile
     * @param errorCallback Optional callback that is invoked when logout fails
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    @JvmOverloads
    fun logout(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.logout(errorCallback)
    }

    /**
     * Restores previously purchased products for the current user.
     *
     * This method synchronizes the user's purchase history with the Botsi service
     * and updates the user profile with the restored purchases.
     *
     * @param successCallback Callback that is invoked when products are successfully restored,
     *                        providing the updated user profile
     * @param errorCallback Optional callback that is invoked when product restoration fails
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    @JvmOverloads
    fun restorePurchase(
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.restorePurchase(successCallback, errorCallback)
    }

    /**
     * Retrieves paywall configuration for the specified placement with backend-only data.
     *
     * A paywall represents a monetization screen that can be displayed to users.
     * This method returns only backend parameters without Google logic.
     *
     * @param placementId The identifier for the paywall placement
     * @param successCallback Callback that is invoked when the paywall is successfully retrieved
     * @param errorCallback Optional callback that is invoked when paywall retrieval fails
     * @throws IllegalStateException if the SDK has not been activated
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
     * Retrieves paywall configuration for the specified placement with Google products.
     *
     * A paywall represents a monetization screen that can be displayed to users.
     * This method returns products enriched with Google Play Store data.
     *
     * @param successCallback Callback that is invoked when the paywall is successfully retrieved
     * @param errorCallback Optional callback that is invoked when paywall retrieval fails
     * @throws IllegalStateException if the SDK has not been activated
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
     * This method fetches the UI configuration that defines how the paywall should be displayed.
     *
     * @param paywallId The unique identifier of the paywall
     * @param successCallback Callback that is invoked when the configuration is successfully retrieved
     * @param errorCallback Optional callback that is invoked when configuration retrieval fails
     * @throws IllegalStateException if the SDK has not been activated
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
     * and handles the purchase completion and verification.
     *
     * @param activity The activity from which the purchase flow is launched
     * @param product The product to be purchased
     * @param subscriptionUpdateParams Optional parameters for subscription updates or replacements
     * @param isOfferPersonalized Whether the offer is personalized to the user (for compliance with regulations)
     * @param callback Callback that is invoked when the purchase is completed, providing the updated profile and purchase details
     * @param errorCallback Optional callback that is invoked when the purchase fails
     * @throws IllegalStateException if the SDK has not been activated
     */
    @JvmStatic
    @JvmOverloads
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
        callback: (BotsiPurchase) -> Unit,
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
     * This method should be called whenever a paywall is displayed to track user interactions.
     *
     * @param paywall The paywall that was shown to the user
     * @throws IllegalStateException if the SDK has not been activated
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
     * that the SDK has been initialized with the [activate] method.
     *
     * @throws IllegalStateException if the SDK has not been activated
     */
    private fun checkActivation() {
        if (::facade.isInitialized.not()) {
            throw IllegalStateException("Botsi not activated!")
        }
    }

}
