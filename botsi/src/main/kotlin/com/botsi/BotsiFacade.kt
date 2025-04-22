package com.botsi

import android.app.Activity
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.analytic.AnalyticsEvent
import com.botsi.analytic.AnalyticsTracker
import com.botsi.domain.interactor.products.BotsiProductsInteractor
import com.botsi.domain.interactor.profile.BotsiProfileInteractor
import com.botsi.domain.interactor.purchase.BotsiPurchaseInteractor
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.domain.model.BotsiUpdateProfileParameters
import com.botsi.scope.launch
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
    fun activate(errorCallback: ((Throwable) -> Unit)? = null) {
        launch {
            profileInteractor.getOrCreateProfile()
                .retryIfNecessary()
                .catch { errorCallback?.invoke(it) }
                .collect()
        }
    }

    @JvmSynthetic
    fun getProfile(callback: (BotsiProfile) -> Unit) {
        launch {
            profileInteractor
                .getOrCreateProfile()
                .retryIfNecessary()
                .onEach { callback(it) }
                .collect()
        }
    }

    @JvmSynthetic
    fun updateProfile(
        customerUserId: String?,
        params: BotsiUpdateProfileParameters?,
        resultCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor
                    .updateProfile(customerUserId, params)
            )
                .retryIfNecessary()
                .onEach { result -> resultCallback(result) }
                .catch {
                    errorCallback?.invoke(it)
                }
                .collect()
        }
    }

    fun getProducts(
        successCallback: (List<ProductDetails>) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                productsInteractor.getProductsIds()
                    .retryIfNecessary()
            )
                .onEach { result -> successCallback(result) }
                .catch { errorCallback?.invoke(it) }
                .collect()
        }
    }

    @JvmSynthetic
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
        isOfferPersonalized: Boolean,
        callback: ((Pair<BotsiProfile, Purchase?>?) -> Unit),
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                purchaseInteractor.makePurchase(
                    activity,
                    product,
                    subscriptionUpdateParams,
                    isOfferPersonalized
                )
            )
                .onEach { result -> callback(result) }
                .catch {
                    errorCallback?.invoke(it)
                }
                .collect()
        }
    }

    @JvmSynthetic
    fun syncPurchases(
        callback: ((BotsiProfile) -> Unit),
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                purchaseInteractor.syncPurchases()
            )
                .onEach { result -> callback(result) }
                .catch {
                    errorCallback?.invoke(it)
                }
                .collect()
        }
    }

    @JvmSynthetic
    fun logout(errorCallback: ((Throwable) -> Unit)?) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor.updateProfile(
                    customerUserId = null,
                    params = null
                )
            )
                .catch { errorCallback?.invoke(it) }
                .collect()
        }
    }

    @JvmSynthetic
    fun login(
        customerUserId: String?,
        errorCallback: ((Throwable) -> Unit)?
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                profileInteractor.updateProfile(
                    customerUserId = customerUserId,
                    params = null
                )
            )
                .catch { errorCallback?.invoke(it) }
                .collect()
        }
    }

    fun getPaywall(
        placementId: String,
        successCallback: (BotsiPaywall) -> Unit,
        errorCallback: ((Throwable) -> Unit)?
    ) {
        launch {
            profileInteractor.doOnProfileReady(
                productsInteractor.getPaywall(placementId)
            )
                .onEach { successCallback(it) }
                .catch { errorCallback?.invoke(it) }
                .collect()
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

    private fun <T> Flow<T>.retryIfNecessary(maxAttemptCount: Long = RETRY_DEFAULT_COUNT): Flow<T> =
        this.retryWhen { error, attempt ->
            error is BotsiException && (maxAttemptCount in 0..attempt)
        }

    private companion object {
        const val RETRY_DEFAULT_COUNT = 3L
    }

}