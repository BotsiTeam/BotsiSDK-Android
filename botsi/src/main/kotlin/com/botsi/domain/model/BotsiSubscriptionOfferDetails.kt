package com.botsi.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

/**
 * Wrapper class for [ProductDetails.SubscriptionOfferDetails]
 */
@Keep
data class BotsiSubscriptionOfferDetails(
    val basePlanId: String,
    val offerId: String?,
    val offerToken: String,
    val pricingPhases: List<BotsiPricingPhase>
) {
    companion object {
        /**
         * Creates a [BotsiSubscriptionOfferDetails] from [ProductDetails.SubscriptionOfferDetails]
         */
        fun from(subscriptionOfferDetails: ProductDetails.SubscriptionOfferDetails?): BotsiSubscriptionOfferDetails? {
            return subscriptionOfferDetails?.let {
                BotsiSubscriptionOfferDetails(
                    basePlanId = it.basePlanId,
                    offerId = it.offerId,
                    offerToken = it.offerToken,
                    pricingPhases = it.pricingPhases.pricingPhaseList.mapNotNull { phase ->
                        BotsiPricingPhase.from(phase)
                    }
                )
            }
        }
    }
}
