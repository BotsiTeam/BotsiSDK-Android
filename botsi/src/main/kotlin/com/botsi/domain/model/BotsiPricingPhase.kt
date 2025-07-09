package com.botsi.domain.model

import com.android.billingclient.api.ProductDetails

/**
 * Wrapper class for [ProductDetails.PricingPhase]
 */
data class BotsiPricingPhase(
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val billingPeriod: String,
    val recurrenceMode: Int,
    val billingCycleCount: Int,
    val formattedPrice: String
) {
    companion object {
        /**
         * Creates a [BotsiPricingPhase] from [ProductDetails.PricingPhase]
         */
        fun from(pricingPhase: ProductDetails.PricingPhase?): BotsiPricingPhase? {
            return pricingPhase?.let {
                BotsiPricingPhase(
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