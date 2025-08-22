package com.botsi.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetails.RecurrenceMode

/**
 * Wrapper class for [ProductDetails.SubscriptionOfferDetails]
 */
@Keep
data class BotsiSubscriptionOfferDetails(
    val basePlanId: String,
    val offerId: String?,
    val offerToken: String,
    val offerTags: List<String>,
    val renewalType: BotsiSubscriptionRenewalType,
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
                    offerTags = it.offerTags,
                    offerToken = it.offerToken,
                    renewalType = when (subscriptionOfferDetails.pricingPhases.pricingPhaseList.lastOrNull()?.recurrenceMode) {
                        RecurrenceMode.NON_RECURRING -> BotsiSubscriptionRenewalType.Prepaid
                        else -> BotsiSubscriptionRenewalType.Autorenewable
                    },
                    pricingPhases = it.pricingPhases.pricingPhaseList.mapNotNull { phase ->
                        BotsiPricingPhase.from(phase)
                    }
                )
            }
        }
    }
}
