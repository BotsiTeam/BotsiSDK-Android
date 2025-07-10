package com.botsi.data.model.request

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiValidateReceiptRequest(
    @SerializedName("profileId")
    val profileId: String,
    @SerializedName("token")
    val token: String?,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("placementId")
    val placementId: String,
    @SerializedName("paywallId")
    val paywallId: Long,
    @SerializedName("abTestId")
    val abTestId: Long,
    @SerializedName("isSubscription")
    val isSubscription: Boolean,
    @SerializedName("oneTimePurchaseOfferDetails")
    val oneTimePurchaseOfferDetails: BotsiOneTimePurchaseOfferDetailsRequestData? = null,
    @SerializedName("subscriptionOfferDetails")
    val subscriptionOfferDetails: BotsiSubscriptionOfferDetailsRequestData? = null,
) {

    @Keep
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    internal class BotsiOneTimePurchaseOfferDetailsRequestData(
        @SerializedName("priceAmountMicros")
        val priceAmountMicros: Long,
        @SerializedName("currencyCode")
        val currencyCode: String,
    )

    @Keep
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    internal class BotsiSubscriptionOfferDetailsRequestData(
        @SerializedName("basePlanId")
        val basePlanId: String,
        @SerializedName("offerId")
        val offerId: String?,
        @SerializedName("pricingPhases")
        val pricingPhases: List<BotsiPricingPhaseRequestData>,
    )

    @Keep
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    internal class BotsiPricingPhaseRequestData(
        @SerializedName("priceAmountMicros")
        val priceAmountMicros: Long,
        @SerializedName("currencyCode")
        val currencyCode: String,
        @SerializedName("billingPeriod")
        val billingPeriod: String,
        @SerializedName("recurrenceMode")
        val recurrenceMode: Long,
        @SerializedName("billingCycleCount")
        val billingCycleCount: Long,
    )


}
