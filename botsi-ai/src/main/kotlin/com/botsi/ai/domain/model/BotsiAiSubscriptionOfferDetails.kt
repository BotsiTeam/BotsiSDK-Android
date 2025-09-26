package com.botsi.ai.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

@Keep
data class BotsiAiSubscriptionOfferDetails(
    val basePlanId: String,
    val offerId: String?,
    val offerToken: String,
    val offerTags: List<String>,
    val renewalType: BotsiAiSubscriptionRenewalType,
    val pricingPhases: List<BotsiAiPricingPhase>
) {
    companion object {
        /**
         * Creates a [BotsiAiSubscriptionOfferDetails] from [com.android.billingclient.api.ProductDetails.SubscriptionOfferDetails]
         */
        fun from(subscriptionOfferDetails: ProductDetails.SubscriptionOfferDetails?): BotsiAiSubscriptionOfferDetails? {
            return subscriptionOfferDetails?.let {
                BotsiAiSubscriptionOfferDetails(
                    basePlanId = it.basePlanId,
                    offerId = it.offerId,
                    offerTags = it.offerTags,
                    offerToken = it.offerToken,
                    renewalType = when (subscriptionOfferDetails.pricingPhases.pricingPhaseList.lastOrNull()?.recurrenceMode) {
                        ProductDetails.RecurrenceMode.NON_RECURRING -> BotsiAiSubscriptionRenewalType.Prepaid
                        else -> BotsiAiSubscriptionRenewalType.Autorenewable
                    },
                    pricingPhases = it.pricingPhases.pricingPhaseList.mapNotNull { phase ->
                        BotsiAiPricingPhase.from(phase)
                    }
                )
            }
        }
    }
}