package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiRestoreProductInfoRequest(
    val attributes: BotsiAiRestoreProductAttributesDto
)

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiRestoreProductAttributesDto(
    val profileId: String,
    val items: List<BotsiAiRestoreProductDto>,
)

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiRestoreProductDto(
    val productId: String,
    val purchaseToken: String,
    val productDetails: BotsiAiRestoreProductDetailsDto,
)

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiRestoreProductDetailsDto(
    val oneTimePurchaseOfferDetails: BotsiAiValidatePurchaseDto.OneTimePurchaseOfferDetails?,
    val subscriptionOfferDetails: List<BotsiAiValidatePurchaseDto.SubscriptionOfferDetails>?,
)

fun createSyncProductInfoRequest(
    profileId: String,
    purchases: List<Pair<BotsiAiPurchaseRecordDto, ProductDetails>>
) = BotsiAiRestoreProductInfoRequest(
    attributes = BotsiAiRestoreProductAttributesDto(
        profileId = profileId,
        items = purchases.map {
            BotsiAiRestoreProductDto(
                productId = it.second.productId,
                purchaseToken = it.first.purchaseToken,
                productDetails = BotsiAiRestoreProductDetailsDto(
                    oneTimePurchaseOfferDetails =
                        it.second.oneTimePurchaseOfferDetails?.let { purchase ->
                            BotsiAiValidatePurchaseDto.OneTimePurchaseOfferDetails(
                                purchase.priceAmountMicros,
                                purchase.priceCurrencyCode,
                            )
                        },
                    subscriptionOfferDetails = it.second.subscriptionOfferDetails
                        ?.map { sub ->
                            BotsiAiValidatePurchaseDto.SubscriptionOfferDetails(
                                basePlanId = sub.basePlanId,
                                offerId = sub.offerId,
                                pricingPhases = sub.pricingPhases.pricingPhaseList.map { pricingPhase ->
                                    BotsiAiValidatePurchaseDto.PricingPhase(
                                        pricingPhase.priceAmountMicros,
                                        pricingPhase.priceCurrencyCode,
                                        pricingPhase.billingPeriod,
                                        pricingPhase.recurrenceMode,
                                        pricingPhase.billingCycleCount,
                                    )
                                }
                            )
                        }
                )
            )
        }
    )
)
