package com.botsi.domain.model

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPurchasableProduct(
    val productId: String,
    val type: String,
    val isConsumable: Boolean,
    val paywallId: Long,
    val placementId: String,
    val abTestId: Long,
    val currentSubOfferDetails: ProductDetails.SubscriptionOfferDetails?,
    val currentOneTmeOfferDetails: ProductDetails.OneTimePurchaseOfferDetails?,
    val isOfferPersonalized: Boolean,
    val productDetails: ProductDetails,
)