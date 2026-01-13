package com.botsi

import android.app.Activity
import androidx.annotation.RestrictTo
import com.botsi.analytic.AnalyticsEvent
import com.botsi.analytic.AnalyticsTracker
import com.botsi.domain.interactor.products.BotsiProductsInteractor
import com.botsi.domain.interactor.profile.BotsiProfileInteractor
import com.botsi.domain.interactor.purchase.BotsiPurchaseInteractor
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.domain.model.BotsiUpdateProfileParameters
import com.botsi.scope.launch
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen

/**
 * Internal facade class that coordinates various interactors to provide SDK functionality.
 *
 * This class acts as a central hub between the public [Botsi] object and the internal
 * business logic implemented in various interactors (profile, purchase, products).
 * It handles coroutine launching, error propagation, and retry logic for SDK operations.
 *
 * @property profileInteractor Interactor for user profile management.
 * @property purchaseInteractor Interactor for handling purchase and restore operations.
 * @property productsInteractor Interactor for retrieving paywall and product information.
 * @property analyticsTracker Tracker for logging SDK events and analytics.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiFacade(
    private val profileInteractor: BotsiProfileInteractor,
    private val purchaseInteractor: BotsiPurchaseInteractor,
    private val productsInteractor: BotsiProductsInteractor,
    private val analyticsTracker: AnalyticsTracker,
) {

    /**
     * Activates the SDK for a user.
     *
     * @param customerUserId Optional user identifier to associate with the profile.
     * @param successCallback Callback invoked when activation is complete.
     * @param errorCallback Callback invoked if activation fails.
     */
    @JvmSynthetic
    fun activate(
        customerUserId: String?,
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.getOrCreateProfile(customerUserId)
                .flatMapConcat { profile ->
                    purchaseInteractor.syncPurchases()
                        .catch { emit(profile) }
                }
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Retrieves the current user profile.
     */
    @JvmSynthetic
    fun getProfile(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor
                .getOrCreateProfile(null)
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Updates user profile attributes.
     */
    @JvmSynthetic
    fun updateProfile(
        params: BotsiUpdateProfileParameters?,
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor
                    .updateProfile(null, params)
            )
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback(it) }
        }
    }

    /**
     * Initiates a purchase flow for a product.
     */
    @JvmSynthetic
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
        successCallback: ((BotsiProfile, BotsiPurchase) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                purchaseInteractor.makePurchase(
                    activity,
                    product,
                    subscriptionUpdateParams,
                )
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it.first, it.second) }
        }
    }

    /**
     * Restores previous purchases for the user.
     */
    @JvmSynthetic
    fun restorePurchases(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                purchaseInteractor.syncPurchases(true)
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Logs out the current user and clears user-specific data.
     */
    @JvmSynthetic
    fun logout(
        successCallback: (() -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor.updateProfile(
                    customerUserId = null,
                    params = null
                )
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke() }
        }
    }

    /**
     * Identifies the user with a specific customer identifier.
     */
    @JvmSynthetic
    fun identify(
        customerUserId: String?,
        successCallback: (() -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor.updateProfile(
                    customerUserId = customerUserId,
                    params = null
                )
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke() }
        }
    }

    /**
     * Retrieves paywall configuration for a placement.
     */
    fun getPaywall(
        placementId: String,
        successCallback: ((BotsiPaywall) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                productsInteractor.getPaywall(placementId)
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Retrieves products associated with a paywall, including store pricing.
     */
    fun getPaywallProducts(
        paywall: BotsiPaywall,
        successCallback: ((List<BotsiProduct>) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                productsInteractor.getPaywallProducts(paywall)
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Retrieves UI configuration for a paywall.
     */
    fun getPaywallViewConfiguration(
        paywallId: Long,
        successCallback: ((JsonElement) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                productsInteractor.getPaywallViewConfiguration(paywallId)
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    /**
     * Logs a paywall impression event.
     */
    fun logShowPaywall(
        paywall: BotsiPaywall,
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                flow {
                    emit(paywall)
                }
            )
                .onEach {
                    analyticsTracker.trackEvent(
                        AnalyticsEvent(
                            placementId = paywall.placementId,
                            paywallId = paywall.id.toString(),
                            abTestId = paywall.abTestId,
                            eventType = "paywall_shown",
                        )
                    )
                }
                .retryIfNecessary()
                .collect()
        }
    }

    /**
     * Clears all locally cached SDK data.
     */
    fun clearCache() {
        launch {
            profileInteractor.clearCache()
        }
    }

    private fun <T> Flow<T>.retryIfNecessary(maxAttemptCount: Long = RETRY_DEFAULT_COUNT): Flow<T> =
        this.retryWhen { error, attempt ->
            error is BotsiException && (maxAttemptCount in 0..attempt)
        }

    private companion object {
        const val RETRY_DEFAULT_COUNT = 3L
    }

}
