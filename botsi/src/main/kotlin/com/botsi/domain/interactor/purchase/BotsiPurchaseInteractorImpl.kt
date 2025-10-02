package com.botsi.domain.interactor.purchase

import android.app.Activity
import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.botsi.BotsiException
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.model.dto.BotsiSyncPurchaseDto
import com.botsi.data.repository.BotsiRepository
import com.botsi.domain.interactor.profile.BotsiProfileInteractor
import com.botsi.domain.mapper.toDomain
import com.botsi.domain.mapper.toDto
import com.botsi.domain.mapper.toPurchasableProduct
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiPurchaseInteractorImpl(
    private val profileInteractor: BotsiProfileInteractor,
    private val googlePlayManager: BotsiGoogleStoreManager,
    private val repository: BotsiRepository,
) : BotsiPurchaseInteractor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
    ): Flow<Pair<BotsiProfile, BotsiPurchase>> {
        return googlePlayManager.queryInfoForProduct(product.productId, product.type)
            .flatMapConcat { productDetails ->
                val purchasableProduct = product
                    .toPurchasableProduct(
                        productDetails,
                    )
                flow {
                    emit(
                        makePurchase(
                            activity,
                            purchasableProduct,
                            subscriptionUpdateParams,
                        )
                    )
                }
                    .flatMapConcat { purchase ->
                        if (purchase != null) {
                            // Use the BotsiPurchase returned by validatePurchase
                            validatePurchase(BotsiPurchase.from(purchase)!!, purchasableProduct)
                        } else {
                            googlePlayManager.findActivePurchaseForProduct(
                                product.productId,
                                product.type,
                            ).flatMapConcat { purchase ->
                                if (purchase == null) throw BotsiException(
                                    message = "Purchase is null",
                                    code = BillingResponseCode.BILLING_UNAVAILABLE
                                )
                                // Use the BotsiPurchase returned by validatePurchase
                                validatePurchase(
                                    BotsiPurchase.from(purchase)!!,
                                    purchasableProduct
                                )
                            }
                        }
                    }
            }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun validatePurchase(
        purchase: BotsiPurchase,
        product: BotsiPurchasableProduct,
    ): Flow<Pair<BotsiProfile, BotsiPurchase>> =
        repository.validatePurchase(purchase, product.toDto())
            .onEach {
                googlePlayManager.acknowledgeOrConsume(purchase, product.isConsumable)
                    .catch { }
                    .collect()
            }
            .map { profile -> profile.toDomain() to purchase }
            .onEach {
                val unsyncedPurchases = repository.getSyncedPurchases().toMutableList()
                unsyncedPurchases.add(
                    BotsiSyncPurchaseDto(
                        purchaseToken = purchase.purchaseToken,
                        purchaseTime = purchase.purchaseTime,
                        products = purchase.products,
                        type = product.type,
                    )
                )
                repository.saveSyncedPurchases(unsyncedPurchases)
            }

    private suspend fun makePurchase(
        activity: Activity,
        purchaseableProduct: BotsiPurchasableProduct,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
    ) = suspendCancellableCoroutine { continuation ->
        googlePlayManager.makePurchase(
            activity,
            purchaseableProduct.toDto(),
            subscriptionUpdateParams?.toDto(),
        ) { purchase, error ->
            if (error == null) {
                continuation.resumeWith(Result.success(purchase))
            } else {
                continuation.resumeWithException(error)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun syncPurchases(byUser: Boolean): Flow<BotsiProfile> {
        return googlePlayManager.getPurchaseHistoryDataToRestore()
            .zip(flow { emit(repository.getSyncedPurchases()) }) { p1, p2 ->
                p1 to p2
            }
            .flatMapLatest { (historyData, syncedPurchases) ->
                val dataToSync = when {
                    byUser -> historyData
                    else -> {
                        historyData.filter { historyRecord ->
                            syncedPurchases.firstOrNull { dto ->
                                dto.purchaseToken == historyRecord.purchaseToken &&
                                        dto.purchaseTime == historyRecord.purchaseTime
                            } == null
                        }
                    }
                }
                if (dataToSync.isNotEmpty()) {
                    googlePlayManager.queryProductDetails(
                        productList = dataToSync.mapNotNull { it.products.firstOrNull() }
                    )
                        .flatMapConcat { marketDetails ->
                            repository.syncPurchases(
                                marketDetails.map {
                                    dataToSync.first { data ->
                                        data.products.firstOrNull() == it.productId
                                    } to it
                                }
                            )
                                .onEach {
                                    val syncedPurchases =
                                        repository.getSyncedPurchases().toMutableList()
                                    syncedPurchases.addAll(
                                        dataToSync.map {
                                            BotsiSyncPurchaseDto(
                                                purchaseToken = it.purchaseToken,
                                                purchaseTime = it.purchaseTime,
                                                products = it.products,
                                                type = it.type,
                                            )
                                        }
                                    )
                                    repository.saveSyncedPurchases(syncedPurchases)
                                }
                        }
                } else {
                    flowOf(null)
                }
            }
            .map { profileInteractor.profileFlow.first() }
    }

}
