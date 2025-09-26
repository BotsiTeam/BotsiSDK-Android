package com.botsi.ai.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

@Keep
data class BotsiAiPricingPhase(
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val billingPeriod: String,
    val recurrenceMode: Int,
    val billingCycleCount: Int,
    val formattedPrice: String
) {
    companion object {
        /**
         * Creates a [BotsiAiPricingPhase] from [ProductDetails.PricingPhase]
         */
        fun from(pricingPhase: ProductDetails.PricingPhase?): BotsiAiPricingPhase? {
            return pricingPhase?.let {
                BotsiAiPricingPhase(
                    priceAmountMicros = it.priceAmountMicros,
                    priceCurrencyCode = it.priceCurrencyCode,
                    billingPeriod = it.billingPeriod,
                    recurrenceMode = it.recurrenceMode,
                    billingCycleCount = it.billingCycleCount,
                    formattedPrice = it.formattedPrice
                )
            }
        }
    }
}
