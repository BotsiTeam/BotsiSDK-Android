package com.botsi.domain.interactor.purchase

import android.app.Activity
import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.botsi.BotsiException
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.model.dto.BotsiUnsyncPurchaseDto
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
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
                googlePlayManager.acknowledgeOrConsume(purchase, product.toDto())
                    .catch { }
                    .collect()
            }
            .map { profile -> profile.toDomain() to purchase }
            .catch {
                val unsyncedPurchases = repository.getUnsyncedPurchases().toMutableList()
                unsyncedPurchases.add(
                    BotsiUnsyncPurchaseDto(
                        purchase = purchase,
                        product = product,
                    )
                )
                repository.saveUnsyncedPurchases(unsyncedPurchases)
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
    override fun syncPurchases(): Flow<BotsiProfile> {
        return flow { emit(repository.getUnsyncedPurchases()) }
            .flatMapLatest { unsyncPurchases ->
                if (unsyncPurchases.isNotEmpty()) {
                    merge(
                        *unsyncPurchases.map {
                            // Use the BotsiPurchase returned by validatePurchase, but only keep the profile
                            validatePurchase(it.purchase, it.product)
                                .map { (profile, _) -> profile }
                        }.toTypedArray()
                    )
                } else {
                    flowOf(null)
                }
            }
            .flatMapMerge {
                googlePlayManager.getPurchaseHistoryDataToRestore()
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

}
