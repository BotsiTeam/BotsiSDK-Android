package com.botsi

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.di.DiManager
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.domain.model.BotsiUpdateProfileParameters
import com.google.gson.JsonElement

object Botsi {

    private lateinit var facade: BotsiFacade
    private val diManager = DiManager()

    @JvmStatic
    @JvmOverloads
    fun activate(
        context: Context,
        apiKey: String,
        customerUserId: String? = null,
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

        facade.activate(
            customerUserId,
            successCallback,
            errorCallback,
        )
    }

    @JvmStatic
    fun getProfile(
        customerUserId: String?,
        successCallback: (BotsiProfile) -> Unit
    ) {
        checkActivation()
        facade.getProfile(
            customerUserId,
            successCallback
        )
    }

    @JvmSynthetic
    @JvmOverloads
    fun updateProfile(
        customerUserId: String?,
        params: BotsiUpdateProfileParameters?,
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.updateProfile(
            customerUserId,
            params,
            successCallback,
            errorCallback
        )
    }

    @JvmStatic
    @JvmOverloads
    fun login(
        customerUserId: String?,
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        checkActivation()
        facade.login(
            customerUserId,
            successCallback,
            errorCallback
        )
    }

    @JvmStatic
    @JvmOverloads
    fun logout(
        successCallback: ((BotsiProfile) -> Unit)? = null,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.logout(
            successCallback,
            errorCallback
        )
    }

    @JvmStatic
    @JvmOverloads
    fun getProducts(
        successCallback: (List<ProductDetails>) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.getProducts(successCallback, errorCallback)
    }

    @JvmStatic
    @JvmOverloads
    fun restoreProducts(
        successCallback: (BotsiProfile) -> Unit,
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.syncPurchases(successCallback, errorCallback)
    }

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

    @JvmStatic
    @JvmOverloads
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        offer: ProductDetails.SubscriptionOfferDetails? = null,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
        isOfferPersonalized: Boolean = false,
        callback: ((Pair<BotsiProfile, Purchase?>?) -> Unit),
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.makePurchase(
            activity,
            product,
            offer,
            subscriptionUpdateParams,
            isOfferPersonalized,
            callback,
            errorCallback
        )
    }

    @JvmStatic
    fun logShowPaywall(paywall: BotsiPaywall) {
        checkActivation()
        facade.logShowPaywall(paywall)
    }

    @JvmStatic
    fun clearCache() {
        facade.clearCache()
    }

    private fun checkActivation() {
        if (::facade.isInitialized.not()) {
            throw IllegalStateException("Botsi not activated!")
        }
    }

}