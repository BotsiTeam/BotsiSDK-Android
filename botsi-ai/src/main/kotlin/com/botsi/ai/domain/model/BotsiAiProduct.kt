package com.botsi.ai.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.ProductDetails

@Keep
data class BotsiAiProduct(
    val botsiProductId: Int,
    val sourceProductId: String,
    val isConsumable: Boolean,
    val basePlanId: String,
    val offerIds: List<String>,
    val productDetails: ProductDetails,
)