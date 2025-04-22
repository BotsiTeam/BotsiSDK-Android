package com.botsi.data.google_store

import android.app.Activity
import android.content.Context
import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.SubscriptionUpdateParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.GetBillingConfigParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.botsi.BotsiException
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiSubscriptionUpdateParametersDto
import com.botsi.domain.model.BotsiReplacementMode
import com.botsi.scope.flowOnMain
import com.botsi.scope.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Semaphore
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.coroutines.resumeWithException

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiGoogleStoreManager(
    context: Context,
) : PurchasesUpdatedListener {

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases(
            PendingPurchasesParams
                .newBuilder()
                .enablePrepaidPlans()
                .enableOneTimeProducts()
                .build()
        )
        .setListener(this)
        .build()

    private val storeHelper = BotsiGoogleStoreHelper(billingClient)

    private var purchaseCallback: BotsiPurchaseCallback? = null

    @JvmSynthetic
    fun getPurchaseHistoryDataToRestore(maxAttemptCount: Long = DEFAULT_RETRY_COUNT): Flow<List<BotsiPurchaseRecordDto>> =
        getPurchaseHistoryDataToRestoreForType(BillingClient.ProductType.SUBS, maxAttemptCount)
            .flatMapConcat { subsHistoryList ->
                getPurchaseHistoryDataToRestoreForType(BillingClient.ProductType.INAPP, maxAttemptCount)
                    .map { inAppHistoryList -> concatResults(subsHistoryList, inAppHistoryList) }
            }

    private fun getPurchaseHistoryDataToRestoreForType(
        @BillingClient.ProductType type: String,
        maxAttemptCount: Long,
    ): Flow<List<BotsiPurchaseRecordDto>> {
        return onConnected {
            storeHelper.queryAllPurchasesForType(type)
                .map { (historyRecords, activePurchases) ->
                    activePurchases
                        .map { purchase ->
                            BotsiPurchaseRecordDto(
                                purchase.purchaseToken,
                                purchase.purchaseTime,
                                purchase.products,
                                type,
                            )
                        } + historyRecords
                        .map { historyRecord ->
                            BotsiPurchaseRecordDto(
                                historyRecord.purchaseToken,
                                historyRecord.purchaseTime,
                                historyRecord.products,
                                type,
                            )
                        }
                        .toSet()
                        .toList()
                }
        }.retryOnConnectionError(maxAttemptCount)
    }

    @JvmSynthetic
    fun queryProductDetails(
        productList: List<String>,
        maxAttemptCount: Long = DEFAULT_RETRY_COUNT,
    ): Flow<List<ProductDetails>> =
        queryProductDetailsForType(
            productList,
            BillingClient.ProductType.SUBS,
            maxAttemptCount
        )
            .flatMapConcat { subsList ->
                queryProductDetailsForType(
                    productList,
                    BillingClient.ProductType.INAPP,
                    maxAttemptCount
                ).map { inAppList -> concatResults(subsList, inAppList) }
            }

    private fun queryProductDetailsForType(
        productList: List<String>,
        @BillingClient.ProductType productType: String,
        maxAttemptCount: Long,
    ): Flow<List<ProductDetails>> {
        return onConnected {
            storeHelper.queryProductDetailsForType(productList, productType)
        }.retryOnConnectionError(maxAttemptCount)
    }

    private fun <T> concatResults(list1: List<T>, list2: List<T>): List<T> =
        ArrayList(list1).apply { addAll(list2) }

    private fun onError(
        billingResult: BillingResult,
        callback: BotsiPurchaseCallback?
    ) {
        val message = storeHelper.errorMessageFromBillingResult(billingResult, "on purchases updated")
        Logger.getAnonymousLogger().log(Level.INFO, message)
        callback?.invoke(
            null,
            BotsiException(
                message = message,
                code = billingResult.responseCode
            )
        )
    }

    private fun onError(
        error: Throwable,
        callback: BotsiPurchaseCallback?,
    ) {
        val message = error.message ?: error.localizedMessage ?: "Unknown billing error occured"
        Logger.getAnonymousLogger().log(Level.INFO, message)
        callback?.invoke(
            null,
            (error as? BotsiException) ?: BotsiException(
                cause = error,
                message = message,
            )
        )
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        when (billingResult.responseCode) {
            BillingResponseCode.OK -> {
                if (purchases == null) {
                    purchaseCallback?.invoke(null, null)
                    return
                }

                for (purchase in purchases) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        purchaseCallback?.invoke(purchase, null)
                    } else {
                        purchaseCallback?.invoke(
                            purchase, BotsiException(
                                message = "Purchase: PENDING_PURCHASE",
                                code = billingResult.responseCode
                            )
                        )
                    }
                }
            }

            BillingResponseCode.USER_CANCELED -> {
                purchaseCallback?.invoke(
                    null,
                    BotsiException(
                        message = "Purchase: USER_CANCELED",
                        code = billingResult.responseCode
                    )
                )
            }

            else -> {
                onError(billingResult, purchaseCallback)
            }
        }
    }

    @JvmSynthetic
    fun queryInfoForProduct(productId: String, type: String) =
        onConnected {
            storeHelper.queryProductDetailsForType(listOf(productId), extractGoogleType(type))
        }.map { productDetailsList ->
            productDetailsList.firstOrNull { it.productId == productId }
                ?: throw BotsiException(message = "This product_id was not found with this purchase type")
        }

    @JvmSynthetic
    fun makePurchase(
        activity: Activity,
        purchaseableProduct: BotsiPurchasableProductDto,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParametersDto?,
        callback: BotsiPurchaseCallback
    ) {
        launch {
            if (subscriptionUpdateParams != null) {
                onConnected {
                    storeHelper.queryActivePurchasesForTypeWithSync(BillingClient.ProductType.SUBS)
                        .map { activeSubscriptions ->
                            buildSubscriptionUpdateParams(
                                activeSubscriptions,
                                subscriptionUpdateParams,
                            ).let { updateParams -> purchaseableProduct.productDetails to updateParams }
                        }
                }
            } else {
                flowOf(purchaseableProduct.productDetails to null)
            }
                .catch { error ->
                    onError(error, callback)
                }
                .onEach { (productDetails, billingFlowSubUpdateParams) ->
                    purchaseCallback = callback

                    val params = BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .apply { purchaseableProduct.currentSubOfferDetails?.offerToken?.let(::setOfferToken) }
                        .build()

                    billingClient.launchBillingFlow(
                        activity,
                        BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(listOf(params))
                            .apply {
                                purchaseableProduct.isOfferPersonalized.takeIf { it }?.let(::setIsOfferPersonalized)
                                billingFlowSubUpdateParams?.let(::setSubscriptionUpdateParams)
                            }
                            .build()
                    )
                }
                .flowOnMain()
                .collect()
        }
    }

    private fun buildSubscriptionUpdateParams(
        purchasesList: List<Purchase>?,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParametersDto,
    ): SubscriptionUpdateParams =
        purchasesList
            ?.firstOrNull { it.products.firstOrNull() == subscriptionUpdateParams.oldSubVendorProductId }
            ?.let { subToBeReplaced ->
                SubscriptionUpdateParams.newBuilder()
                    .setOldPurchaseToken(subToBeReplaced.purchaseToken)
                    .setSubscriptionReplacementMode(
                        mapReplacementMode(subscriptionUpdateParams.replacementMode)
                    )
                    .build()
            }
            ?: "Can't launch flow to change subscription. Either subscription to change is inactive, or it was purchased from different Google account or from iOS".let { errorMessage ->
                Logger.getAnonymousLogger().log(Level.WARNING, errorMessage)
                throw BotsiException(message = errorMessage)
            }

    @JvmSynthetic
    fun acknowledgeOrConsume(purchase: Purchase, product: BotsiPurchasableProductDto) =
        onConnected {
            if (product.isConsumable) {
                storeHelper.consumePurchase(purchase)
            } else {
                storeHelper.acknowledgePurchase(purchase)
            }
        }
            .retryOnConnectionError(DEFAULT_RETRY_COUNT)

    @JvmSynthetic
    fun getStoreCountry() =
        onConnected {
            val params = GetBillingConfigParams.newBuilder().build()
            storeHelper.getBillingConfig(params)
        }
            .catch { e ->
                Logger.getAnonymousLogger().log(
                    Level.WARNING,
                    e.message ?: e.localizedMessage ?: "Unknown error occured on get billing config"
                )
                throw e
            }
            .map { config -> config?.countryCode }

    @JvmSynthetic
    fun findActivePurchaseForProduct(
        productId: String,
        type: String,
    ) = queryActivePurchasesForType(extractGoogleType(type), DEFAULT_RETRY_COUNT)
        .map { purchases ->
            purchases.firstOrNull {
                it.purchaseState == Purchase.PurchaseState.PURCHASED && it.products.firstOrNull() == productId
            }
        }

    private fun queryActivePurchasesForType(
        @BillingClient.ProductType type: String,
        maxAttemptCount: Long,
    ): Flow<List<Purchase>> {
        return onConnected {
            storeHelper.queryActivePurchasesForType(type)
        }
            .retryOnConnectionError(maxAttemptCount)
    }

    private fun extractGoogleType(type: String) =
        when (type) {
            BillingClient.ProductType.SUBS -> type
            else -> BillingClient.ProductType.INAPP
        }

    private fun <T> onConnected(call: () -> Flow<T>): Flow<T> =
        restoreConnection().flatMapLatest { call() }

    private fun restoreConnection(): Flow<Unit> =
        flow { emit(billingClient.startConnectionSync()) }
            .take(1)

    private val startConnectionSemaphore = Semaphore(1)

    private suspend fun BillingClient.startConnectionSync() {
        startConnectionSemaphore.acquire()
        return suspendCancellableCoroutine { continuation ->
            var resumed = false
            startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (!resumed) {
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            continuation.resume(Unit) {}
                        } else {
                            continuation.resumeWithException(
                                BotsiException(
                                    message = "Play Market request failed: ${billingResult.debugMessage}",
                                    code = billingResult.responseCode
                                )
                            )
                        }
                        resumed = true
                        startConnectionSemaphore.release()
                    }
                }

                override fun onBillingServiceDisconnected() {
                    if (!resumed) {
                        continuation.resumeWithException(
                            BotsiException(
                                message = "Play Market request failed: SERVICE_DISCONNECTED",
                                code = BillingResponseCode.SERVICE_DISCONNECTED
                            )
                        )
                        resumed = true
                        startConnectionSemaphore.release()
                    }
                }
            })
        }
    }

    private fun <T> Flow<T>.retryOnConnectionError(maxAttemptCount: Long = Long.MAX_VALUE): Flow<T> =
        this.retryWhen { error, attempt ->
            if (canRetry(error, attempt, maxAttemptCount)) {
                delay(2000)
                return@retryWhen true
            } else {
                val message = error.message ?: error.localizedMessage ?: "Unknown billing error occured"
                Logger.getAnonymousLogger().log(Level.INFO, message)
                return@retryWhen false
            }
        }

    private fun canRetry(error: Throwable, attempt: Long, maxAttemptCount: Long): Boolean {
        return when {
            maxAttemptCount in 0..attempt -> false
            error !is BotsiException || error.code in arrayOf(
                BillingResponseCode.SERVICE_DISCONNECTED,
                BillingResponseCode.SERVICE_UNAVAILABLE,
                BillingResponseCode.NETWORK_ERROR,
            ) -> true

            error.code == BillingResponseCode.ERROR
                    && ((maxAttemptCount.takeIf { it in 0..DEFAULT_RETRY_COUNT }
                ?: DEFAULT_RETRY_COUNT) > attempt) -> true

            else -> false
        }
    }

    private fun mapReplacementMode(replacementMode: BotsiReplacementMode) =
        when (replacementMode) {
            BotsiReplacementMode.WITH_TIME_PRORATION -> SubscriptionUpdateParams.ReplacementMode.WITH_TIME_PRORATION
            BotsiReplacementMode.WITHOUT_PRORATION -> SubscriptionUpdateParams.ReplacementMode.WITHOUT_PRORATION
            BotsiReplacementMode.CHARGE_PRORATED_PRICE -> SubscriptionUpdateParams.ReplacementMode.CHARGE_PRORATED_PRICE
            BotsiReplacementMode.DEFERRED -> SubscriptionUpdateParams.ReplacementMode.DEFERRED
            BotsiReplacementMode.CHARGE_FULL_PRICE -> SubscriptionUpdateParams.ReplacementMode.CHARGE_FULL_PRICE
        }

    private companion object {
        const val DEFAULT_RETRY_COUNT = 3L
    }
}