package com.botsi.domain.interactor.purchase

import android.app.Activity
import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.Purchase
import com.botsi.BotsiException
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.repository.BotsiRepository
import com.botsi.domain.interactor.profile.BotsiProfileInteractor
import com.botsi.domain.mapper.toDomain
import com.botsi.domain.mapper.toDto
import com.botsi.domain.mapper.toPurchasableProduct
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
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
        isOfferPersonalized: Boolean
    ): Flow<Pair<BotsiProfile, Purchase?>?> {
        return googlePlayManager.queryInfoForProduct(product.productId, product.type)
            .flatMapConcat { productDetails ->
                val purchasableProduct = product
                    .toPurchasableProduct(
                        productDetails,
                        isOfferPersonalized
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
                            validatePurchase(purchase, purchasableProduct)
                        } else {
                            flowOf(null)
                        }
                    }
                    .catch { error ->
                        if (error is BotsiException && error.code == BillingResponseCode.ITEM_UNAVAILABLE) {
                            emitAll(
                                googlePlayManager.findActivePurchaseForProduct(
                                    product.productId,
                                    product.type,
                                ).flatMapConcat { purchase ->
                                    if (purchase == null) throw error
                                    validatePurchase(purchase, purchasableProduct)
                                }
                            )
                        } else {
                            throw error
                        }
                    }
            }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun validatePurchase(
        purchase: Purchase,
        product: BotsiPurchasableProduct,
    ): Flow<Pair<BotsiProfile, Purchase>> =
        repository.validatePurchase(purchase, product.toDto())
            .catch { e ->
                if (e is BotsiException && e.cause != null) {
                    googlePlayManager.acknowledgeOrConsume(purchase, product.toDto())
                        .catch { }
                        .collect()
                }
                throw e
            }
            .map { profile -> profile.toDomain() to purchase }

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
    override fun syncPurchases(): Flow<BotsiProfile> {
        return googlePlayManager.getPurchaseHistoryDataToRestore()
            .map { history ->
                val profile = repository.profileStateFlow.first()

                val alreadyPurchased = profile.subscriptions.orEmpty()
                    .mapNotNull { it.value.sourceProductId } +
                        profile.nonSubscriptions.orEmpty()
                            .map { it.value.sourceProductId }

                history.filter { item ->
                    item.products.none { product ->
                        alreadyPurchased.contains(product)
                    }
                }
            }
            .filter { it.isNotEmpty() }
            .flatMapLatest {
                googlePlayManager.queryProductDetails(
                    it.mapNotNull { it.products.firstOrNull() }
                ).map { products ->
                    products.map { product ->
                        it.first { history ->
                            history.products.contains(product.productId)
                        } to product
                    }
                }
            }
            .flatMapLatest {
                repository.syncPurchases(it)
            }
            .map { it.toDomain() }
            .onEmpty { emit(profileInteractor.profileFlow.first()) }
    }

}