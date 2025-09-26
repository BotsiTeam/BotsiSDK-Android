package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.ai.domain.model.BotsiAiSubscriptionOfferDetails

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class BotsiAiPurchasableProductDto(
    val isConsumable: Boolean,
    val currentSubOfferDetails: BotsiAiSubscriptionOfferDetails?,
    val productDetails: ProductDetails,
)
