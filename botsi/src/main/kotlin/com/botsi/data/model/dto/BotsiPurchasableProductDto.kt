package com.botsi.data.model.dto

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.domain.model.BotsiOneTimePurchaseOfferDetails
import com.botsi.domain.model.BotsiSubscriptionOfferDetails

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPurchasableProductDto(
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
