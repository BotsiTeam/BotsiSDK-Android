package com.botsi.data.google_store

import androidx.annotation.RestrictTo
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode.OK
import com.android.billingclient.api.BillingConfig
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.GetBillingConfigParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchaseHistoryParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.acknowledgePurchase
import com.android.billingclient.api.consumePurchase
import com.android.billingclient.api.queryProductDetails
import com.android.billingclient.api.queryPurchaseHistory
import com.android.billingclient.api.queryPurchasesAsync
import com.botsi.BotsiException
import com.botsi.domain.model.BotsiPurchase
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiGoogleStoreHelper(
    private val billingClient: BillingClient,
) {

    @JvmSynthetic
    fun queryProductDetailsForType(productList: List<String>, @BillingClient.ProductType productType: String) =
        flow {
            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(
                    productList.map { productId ->
                        QueryProductDetailsParams.Product.newBuilder()
                            .setProductId(productId)
                            .setProductType(productType)
                            .build()
                    }
                )
                .build()
            val productDetailsResult = billingClient.queryProductDetails(params)

            if (productDetailsResult.billingResult.responseCode == OK) {
                emit(productDetailsResult.productDetailsList.orEmpty())
            } else {
                val e = createException(productDetailsResult.billingResult, "on query product details")
                throw e
            }
        }

    @JvmSynthetic
    fun queryActivePurchasesForType(@BillingClient.ProductType type: String) =
        flow {
            val params = QueryPurchasesParams.newBuilder().setProductType(type).build()
            val purchasesResult = billingClient.queryPurchasesAsync(params)
            if (purchasesResult.billingResult.responseCode == OK) {
                emit(purchasesResult.purchasesList)
            } else {
                val e = createException(purchasesResult.billingResult, "on query active purchases")
                throw e
            }
        }

    @JvmSynthetic
    fun queryPurchaseHistoryForType(@BillingClient.ProductType type: String) =
        flow {
            val params = QueryPurchaseHistoryParams.newBuilder().setProductType(type).build()
            val purchaseHistoryResult = billingClient.queryPurchaseHistory(params)
            if (purchaseHistoryResult.billingResult.responseCode == OK) {
                emit(purchaseHistoryResult.purchaseHistoryRecordList.orEmpty())
            } else {
                val e = createException(purchaseHistoryResult.billingResult, "on query history")
                throw e
            }
        }

    @JvmSynthetic
    fun queryAllPurchasesForType(@BillingClient.ProductType type: String) =
        queryPurchaseHistoryForType(type)
            .flatMapConcat { historyRecords ->
                queryActivePurchasesForType(type)
                    .map { activePurchases ->
                        historyRecords to activePurchases.filter { it.purchaseState == Purchase.PurchaseState.PURCHASED }
                    }
            }

    @JvmSynthetic
    fun queryActivePurchasesForTypeWithSync(@BillingClient.ProductType type: String) =
        queryAllPurchasesForType(type)
            .map { (_, activePurchases) -> activePurchases }

    @JvmSynthetic
    fun acknowledgePurchase(purchase: BotsiPurchase) =
        flow {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            val result = billingClient.acknowledgePurchase(params)
            if (result.responseCode == OK) {
                emit(Unit)
            } else {
                val e = createException(result, "on acknowledge")
                throw e
            }
        }

    @JvmSynthetic
    fun consumePurchase(purchase: BotsiPurchase) =
        flow {
            val params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            val result = billingClient.consumePurchase(params).billingResult
            if (result.responseCode == OK) {
                emit(Unit)
            } else {
                val e = createException(result, "on consume")
                throw e
            }
        }

    @JvmSynthetic
    fun getBillingConfig(params: GetBillingConfigParams) =
        flow {
            val (result, config) = getBillingConfigSync(params)
            if (result.responseCode == OK) {
                emit(config)
            } else {
                throw createException(result, "on get billing config")
            }
        }

    private suspend fun getBillingConfigSync(params: GetBillingConfigParams): Pair<BillingResult, BillingConfig?> {
        return suspendCancellableCoroutine { continuation ->
            var resumed = false
            billingClient.getBillingConfigAsync(params) { billingResult, billingConfig ->
                if (!resumed) {
                    continuation.resume(billingResult to billingConfig) {}
                    resumed = true
                }
            }
        }
    }

    @JvmSynthetic
    fun errorMessageFromBillingResult(billingResult: BillingResult, where: String) =
        "Play Market request failed $where: responseCode=${billingResult.responseCode}${
            billingResult.debugMessage.takeIf(String::isNotEmpty)?.let { msg -> ", debugMessage=$msg" }.orEmpty()
        }"

    private fun createException(billingResult: BillingResult, where: String): BotsiException {
        val message = errorMessageFromBillingResult(billingResult, where)
        return BotsiException(
            message = message,
            code = billingResult.responseCode,
        )
    }
}