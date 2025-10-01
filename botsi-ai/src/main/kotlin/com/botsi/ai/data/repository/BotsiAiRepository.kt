package com.botsi.ai.data.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.ai.common.store.BotsiAiGoogleStoreManager
import com.botsi.ai.data.api.BotsiAiApiService
import com.botsi.ai.data.model.BotsiAiPaywallDto
import com.botsi.ai.data.model.BotsiAiProductDto
import com.botsi.ai.data.model.BotsiAiValidatePurchaseDto
import com.botsi.ai.data.service.BotsiAiInstallationMetaRetrieverService
import com.botsi.ai.data.storage.BotsiAiStorageManager
import com.botsi.ai.domain.mapper.toPurchasableProductDto
import com.botsi.ai.domain.model.BotsiAiPurchase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

interface BotsiAiRepository {

    suspend fun createProfileIfNecessary(secretKey: String)
    suspend fun getPaywall(placementId: String, secretKey: String): BotsiAiPaywallDto
    suspend fun getMarketProducts(ids: List<String>): List<ProductDetails>
    suspend fun makePurchase(
        secretKey: String,
        placementId: String,
        activity: Activity,
        paywall: BotsiAiPaywallDto,
        product: BotsiAiProductDto,
        productDetails: ProductDetails
    ): Boolean

}

class BotsiAiRepositoryImpl(
    private val apiService: BotsiAiApiService,
    private val storeManager: BotsiAiGoogleStoreManager,
    private val installationMetaService: BotsiAiInstallationMetaRetrieverService,
    private val storageManager: BotsiAiStorageManager,
) : BotsiAiRepository {

    override suspend fun createProfileIfNecessary(secretKey: String) {
        if (!storageManager.profileId.isNullOrEmpty()) return

        val profile = apiService.createProfile(
            secretKey = secretKey,
            meta = installationMetaService.installationMetaFlow(storageManager.deviceId).first()
        ).data

        storageManager.profileId = profile.profileId
    }

    override suspend fun getPaywall(
        placementId: String,
        secretKey: String
    ): BotsiAiPaywallDto {
        return apiService.getPaywall(
            secretKey = secretKey,
            body = installationMetaService.getPaywallBodyFlow(
                profileId = storageManager.profileId!!,
                placementId = placementId
            ).first()
        ).data
    }

    override suspend fun getMarketProducts(ids: List<String>): List<ProductDetails> {
        return storeManager.queryProductDetails(productList = ids).first()
    }

    override suspend fun makePurchase(
        secretKey: String,
        placementId: String,
        activity: Activity,
        paywall: BotsiAiPaywallDto,
        product: BotsiAiProductDto,
        productDetails: ProductDetails,
    ): Boolean {
        val makePurchaseChannel = Channel<Purchase?>()
        val purchasableDto = product.toPurchasableProductDto(productDetails)
        storeManager.makePurchase(
            activity = activity,
            subscriptionUpdateParams = null,
            purchaseableProduct = purchasableDto,
            callback = { purchase, error ->
                if (error != null) {
                    makePurchaseChannel.close(error)
                } else {
                    makePurchaseChannel.trySend(purchase)
                }
            },
        )

        val purchase = makePurchaseChannel.receive()

        val status = apiService.validatePayment(
            secretKey = secretKey,
            body = BotsiAiValidatePurchaseDto(
                token = purchase?.purchaseToken,
                placementId = placementId,
                productId = productDetails.productId,
                profileId = storageManager.profileId,
                paywallId = paywall.id,
                isExperiment = paywall.isExperiment,
                aiPricingModelId = paywall.aiPricingModelId,
                oneTimePurchaseOfferDetails = productDetails.oneTimePurchaseOfferDetails?.let {
                    BotsiAiValidatePurchaseDto.OneTimePurchaseOfferDetails(
                        priceAmountMicros = it.priceAmountMicros,
                        currencyCode = it.priceCurrencyCode
                    )
                },
                subscriptionOfferDetails = productDetails.subscriptionOfferDetails?.firstOrNull()
                    ?.let {
                        BotsiAiValidatePurchaseDto.SubscriptionOfferDetails(
                            it.basePlanId,
                            offerId = it.offerId,
                            pricingPhases = it.pricingPhases.pricingPhaseList.map { price ->
                                BotsiAiValidatePurchaseDto.PricingPhase(
                                    priceAmountMicros = price.priceAmountMicros,
                                    currencyCode = price.priceCurrencyCode,
                                    billingPeriod = price.billingPeriod,
                                    recurrenceMode = price.recurrenceMode,
                                    billingCycleCount = price.billingCycleCount
                                )
                            }
                        )
                    },
            )
        ).status

        storeManager.acknowledgeOrConsume(
            purchase = BotsiAiPurchase.from(purchase),
            product = purchasableDto
        )
            .catch { }
            .first()

        return status
    }
}