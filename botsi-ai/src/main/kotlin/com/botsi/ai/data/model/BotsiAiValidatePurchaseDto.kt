package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class BotsiAiValidatePurchaseDto(
    @SerializedName("token") val token: String? = null,
    @SerializedName("placementId") val placementId: String? = null,
    @SerializedName("productId") val productId: String? = null,
    @SerializedName("profileId") val profileId: String? = null,
    @SerializedName("paywallId") val paywallId: Long? = null,
    @SerializedName("isExperiment") val isExperiment: Boolean? = null,
    @SerializedName("aiPricingModelId") val aiPricingModelId: Long? = null,
    @SerializedName("oneTimePurchaseOfferDetails") val oneTimePurchaseOfferDetails: OneTimePurchaseOfferDetails? = null,
    @SerializedName("subscriptionOfferDetails") val subscriptionOfferDetails: SubscriptionOfferDetails? = null
) {

    @Keep
    data class OneTimePurchaseOfferDetails(
        @SerializedName("priceAmountMicros") val priceAmountMicros: Long? = null,
        @SerializedName("currencyCode") val currencyCode: String? = null
    )

    @Keep
    data class SubscriptionOfferDetails(
        @SerializedName("basePlanId") val basePlanId: String? = null,
        @SerializedName("offerId") val offerId: String? = null,
        @SerializedName("pricingPhases") val pricingPhases: List<PricingPhase>? = null
    )

    @Keep
    data class PricingPhase(
        @SerializedName("priceAmountMicros") val priceAmountMicros: Long? = null,
        @SerializedName("currencyCode") val currencyCode: String? = null,
        @SerializedName("billingPeriod") val billingPeriod: String? = null,
        @SerializedName("recurrenceMode") val recurrenceMode: Int? = null,
        @SerializedName("billingCycleCount") val billingCycleCount: Int? = null
    )
}
