package com.botsi.domain.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPurchasableProduct(
    val productId: String,
    val type: String,
    val isConsumable: Boolean,
    val paywallId: Long,
    val placementId: String,
    val abTestId: Long,
    val currentSubOfferDetails: BotsiSubscriptionOfferDetails?,
    val currentOneTmeOfferDetails: BotsiOneTimePurchaseOfferDetails?,
    val productDetails: ProductDetails,
)
