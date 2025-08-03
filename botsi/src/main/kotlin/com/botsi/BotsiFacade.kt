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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiFacade(
    private val profileInteractor: BotsiProfileInteractor,
    private val purchaseInteractor: BotsiPurchaseInteractor,
    private val productsInteractor: BotsiProductsInteractor,
    private val analyticsTracker: AnalyticsTracker,
) {

    @JvmSynthetic
    fun activate(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.getOrCreateProfile()
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

    @JvmSynthetic
    fun getProfile(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor
                .getOrCreateProfile()
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

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

    @JvmSynthetic
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
        successCallback: ((BotsiPurchase) -> Unit)? = null,
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
                .collect { successCallback?.invoke(it) }
        }
    }

    @JvmSynthetic
    fun restorePurchases(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                purchaseInteractor.syncPurchases()
            )
                .catch { errorCallback?.invoke(it) }
                .collect { successCallback?.invoke(it) }
        }
    }

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
