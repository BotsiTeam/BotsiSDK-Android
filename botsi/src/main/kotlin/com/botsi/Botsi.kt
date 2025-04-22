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

object Botsi {

    private lateinit var facade: BotsiFacade
    private val diManager = DiManager()

    @JvmStatic
    @JvmOverloads
    fun activate(
        context: Context,
        apiKey: String,
        errorCallback: ((Throwable) -> Unit)? = null
    ) {
        diManager.initDi(context, apiKey)

        facade = BotsiFacade(
            profileInteractor = diManager.inject(),
            productsInteractor = diManager.inject(),
            purchaseInteractor = diManager.inject(),
            analyticsTracker = diManager.inject(),
        )

        facade.activate(errorCallback)
    }

    @JvmStatic
    @JvmOverloads
    fun login(customerUserId: String?, errorCallback: ((Throwable) -> Unit)? = null) {
        checkActivation()
        facade.login(customerUserId, errorCallback)
    }

    @JvmStatic
    @JvmOverloads
    fun logout(errorCallback: ((Throwable) -> Unit)? = null) {
        checkActivation()
        facade.logout(errorCallback)
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
    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters? = null,
        isOfferPersonalized: Boolean = false,
        callback: ((Pair<BotsiProfile, Purchase?>?) -> Unit),
        errorCallback: ((Throwable) -> Unit)? = null,
    ) {
        checkActivation()
        facade.makePurchase(activity, product, subscriptionUpdateParams, isOfferPersonalized, callback, errorCallback)
    }

    @JvmStatic
    fun logShowPaywall(paywall: BotsiPaywall) {
        checkActivation()
        facade.logShowPaywall(paywall)
    }

    private fun checkActivation() {
        if (::facade.isInitialized.not()) {
            throw IllegalStateException("Botsi not activated!")
        }
    }

}