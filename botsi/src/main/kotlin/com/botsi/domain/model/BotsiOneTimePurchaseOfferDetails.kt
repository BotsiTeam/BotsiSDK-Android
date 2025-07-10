package com.botsi.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

/**
 * Wrapper class for [ProductDetails.OneTimePurchaseOfferDetails]
 */
@Keep
data class BotsiOneTimePurchaseOfferDetails(
    val priceAmountMicros: Long,
    val priceCurrencyCode: String,
    val formattedPrice: String
) {
    companion object {
        /**
         * Creates a [BotsiOneTimePurchaseOfferDetails] from [ProductDetails.OneTimePurchaseOfferDetails]
         */
        fun from(oneTimePurchaseOfferDetails: ProductDetails.OneTimePurchaseOfferDetails?): BotsiOneTimePurchaseOfferDetails? {
            return oneTimePurchaseOfferDetails?.let {
                BotsiOneTimePurchaseOfferDetails(
                    priceAmountMicros = it.priceAmountMicros,
                    priceCurrencyCode = it.priceCurrencyCode,
                    formattedPrice = it.formattedPrice
                )
            }
        }
    }
}
