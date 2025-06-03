package com.botsi.domain.interactor.products

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.repository.BotsiRepository
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.google.gson.JsonElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiProductsInteractorImpl(
    private val repository: BotsiRepository,
    private val googleStoreManager: BotsiGoogleStoreManager,
) : BotsiProductsInteractor {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProductsIds(): Flow<List<ProductDetails>> {
        return repository.getProductIds()
            .flatMapLatest { googleStoreManager.queryProductDetails(it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPaywall(placementId: String): Flow<BotsiPaywall> {
        return repository.getPaywall(placementId)
            .flatMapLatest { paywall ->
                if (!paywall.sourceProducts.isNullOrEmpty()) {
                    googleStoreManager.queryProductDetails(paywall.sourceProducts.mapNotNull { it.sourcePoductId })
                        .flatMapLatest { products ->
                            flow<List<BotsiProduct>> {
                                val finalProducts = mutableListOf<BotsiProduct>()
                                products.forEach { product ->
                                    val dto = paywall.sourceProducts
                                        .find { source -> source.sourcePoductId == product.productId }

                                    product.subscriptionOfferDetails
                                        ?.filter {
                                            dto?.offerIds?.takeIf { list -> list.isNotEmpty() }
                                                ?.contains(it.offerId) != false
                                        }
                                        ?.forEach {
                                            finalProducts.add(
                                                BotsiProduct(
                                                    productId = product.productId,
                                                    paywallName = paywall.name.orEmpty(),
                                                    type = product.productType,
                                                    name = product.name,
                                                    title = product.title,
                                                    description = product.description,
                                                    isConsumable = dto?.isConsumable == true,
                                                    basePlanId = dto?.basePlanId.orEmpty(),
                                                    subscriptionOffer = it,
                                                    onTimePurchaseOffers = null,
                                                    placementId = placementId,
                                                    paywallId = paywall.id ?: 0,
                                                    abTestId = paywall.abTestId ?: 0,
                                                )
                                            )
                                        }

                                    product.oneTimePurchaseOfferDetails?.let {
                                        finalProducts.add(
                                            BotsiProduct(
                                                productId = product.productId,
                                                paywallName = paywall.name.orEmpty(),
                                                type = product.productType,
                                                name = product.name,
                                                title = product.title,
                                                description = product.description,
                                                isConsumable = dto?.isConsumable == true,
                                                basePlanId = dto?.basePlanId.orEmpty(),
                                                subscriptionOffer = null,
                                                onTimePurchaseOffers = it,
                                                placementId = placementId,
                                                paywallId = paywall.id ?: 0,
                                                abTestId = paywall.abTestId ?: 0,
                                            )
                                        )
                                    }

                                }
                                emit(finalProducts)
                            }
                        }
                } else {
                    flowOf(emptyList())
                }.map {
                    BotsiPaywall(
                        id = paywall.id ?: 0L,
                        placementId = placementId,
                        name = paywall.name.orEmpty(),
                        abTestId = paywall.abTestId ?: 0L,
                        remoteConfigs = paywall.remoteConfigs.orEmpty(),
                        revision = paywall.revision ?: 0L,
                        sourceProducts = it
                    )
                }
            }
    }

    override fun getPaywallViewConfiguration(placementId: String, paywallId: Long): Flow<JsonElement> {
        return repository.getPaywallViewConfiguration(placementId, paywallId)
    }
}