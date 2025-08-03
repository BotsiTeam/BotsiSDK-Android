package com.botsi.domain.interactor.products

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.repository.BotsiRepository
import com.botsi.domain.model.BotsiBackendProduct
import com.botsi.domain.model.BotsiOneTimePurchaseOfferDetails
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiSubscriptionOfferDetails
import com.google.gson.JsonElement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
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
            .map { paywall ->
                val backendProducts = paywall.sourceProducts?.map { dto ->
                    BotsiBackendProduct(
                        productId = dto.sourcePoductId.orEmpty(),
                        paywallId = paywall.id ?: 0,
                        abTestId = paywall.abTestId ?: 0,
                        placementId = placementId,
                        paywallName = paywall.name.orEmpty(),
                        isConsumable = dto.isConsumable == true,
                        basePlanId = dto.basePlanId.orEmpty(),
                        offerIds = dto.offerIds.orEmpty(),
                    )
                }.orEmpty()

                BotsiPaywall(
                    id = paywall.id ?: 0L,
                    placementId = placementId,
                    name = paywall.name.orEmpty(),
                    abTestId = paywall.abTestId ?: 0L,
                    remoteConfigs = paywall.remoteConfigs.orEmpty(),
                    revision = paywall.revision ?: 0L,
                    sourceProducts = backendProducts
                )
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPaywallProducts(paywall: BotsiPaywall): Flow<List<BotsiProduct>> {
        return if (paywall.sourceProducts.isNotEmpty()) {
            googleStoreManager.queryProductDetails(paywall.sourceProducts.map { it.productId })
                .map { products ->
                    val finalProducts = mutableListOf<BotsiProduct>()
                    products.forEach { product ->
                        val dto = paywall.sourceProducts
                            .find { source -> source.productId == product.productId }

                        val offers = dto?.offerIds.orEmpty().map {
                            findCurrentOffer(
                                subOfferDetails = product.subscriptionOfferDetails.orEmpty(),
                                basePlanId = dto?.basePlanId.orEmpty(),
                                offerId = it
                            )
                        }.ifEmpty {
                            listOf(product.subscriptionOfferDetails?.lastOrNull())
                        }

                        offers.forEach {
                            finalProducts.add(
                                BotsiProduct(
                                    productId = product.productId,
                                    paywallName = paywall.name,
                                    type = product.productType,
                                    name = product.name,
                                    title = product.title,
                                    description = product.description,
                                    isConsumable = dto?.isConsumable == true,
                                    basePlanId = dto?.basePlanId.orEmpty(),
                                    subscriptionOffer = BotsiSubscriptionOfferDetails.from(it),
                                    onTimePurchaseOffers = BotsiOneTimePurchaseOfferDetails.from(product.oneTimePurchaseOfferDetails),
                                    placementId = paywall.placementId,
                                    paywallId = paywall.id,
                                    abTestId = paywall.abTestId,
                                )
                            )
                        }
                    }
                    finalProducts.toList()
                }
        } else {
            flowOf(emptyList())
        }
    }

    override fun getPaywallViewConfiguration(paywallId: Long): Flow<JsonElement> {
        return repository.getPaywallViewConfiguration(paywallId)
    }

    private fun findCurrentOffer(
        subOfferDetails: List<SubscriptionOfferDetails>,
        basePlanId: String,
        offerId: String,
    ): SubscriptionOfferDetails? {
        var baseOffer: SubscriptionOfferDetails? = null
        for (offer in subOfferDetails) {
            if (offer.basePlanId == basePlanId) {
                if (offer.offerId == offerId) {
                    return offer
                } else if (offer.offerId == null) {
                    baseOffer = offer
                }
            }
        }

        return baseOffer
    }
}
