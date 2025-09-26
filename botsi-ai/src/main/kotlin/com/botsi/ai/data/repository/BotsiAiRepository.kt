package com.botsi.ai.data.repository

import android.app.Activity
import com.android.billingclient.api.ProductDetails
import com.botsi.ai.common.store.BotsiAiGoogleStoreManager
import com.botsi.ai.data.api.BotsiAiApiService
import com.botsi.ai.data.model.BotsiAiPaywallDto
import com.botsi.ai.data.model.BotsiAiProductDto
import com.botsi.ai.data.model.BotsiAiValidatePurchaseDto
import com.botsi.ai.data.service.BotsiAiInstallationMetaRetrieverService
import com.botsi.ai.data.storage.BotsiAiStorageManager
import com.botsi.ai.domain.mapper.toPurchasableProductDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

interface BotsiAiRepository {

    suspend fun createProfileIfNecessary()
    suspend fun getPaywall(placementId: String): BotsiAiPaywallDto
    suspend fun getMarketProducts(ids: List<String>): List<ProductDetails>
    suspend fun makePurchase(
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

    override suspend fun createProfileIfNecessary() {
        if (!storageManager.profileId.isNullOrEmpty()) return

        val profile = apiService.createProfile(
            secretKey = SECRET_KEY,
            meta = installationMetaService.installationMetaFlow(storageManager.deviceId).first()
        ).data

        storageManager.profileId = profile.profileId
    }

    override suspend fun getPaywall(placementId: String): BotsiAiPaywallDto {
        return apiService.getPaywall(
            secretKey = SECRET_KEY,
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
        placementId: String,
        activity: Activity,
        paywall: BotsiAiPaywallDto,
        product: BotsiAiProductDto,
        productDetails: ProductDetails,
    ): Boolean {
        val purchase = suspendCancellableCoroutine { con ->
            storeManager.makePurchase(
                activity = activity,
                subscriptionUpdateParams = null,
                purchaseableProduct = product.toPurchasableProductDto(productDetails),
                callback = { purchase, error ->
                    if (error != null) {
                        con.resumeWithException(error)
                    } else {
                        con.resumeWith(Result.success(purchase))
                    }
                },
            )
        }

        return apiService.validatePayment(
            secretKey = SECRET_KEY,
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
    }

    companion object {
        private const val SECRET_KEY = "sk_ElLDOD5E8Eq4v5y.XlJOTtEIqK9bL15J3C7ofuxag"
    }

}