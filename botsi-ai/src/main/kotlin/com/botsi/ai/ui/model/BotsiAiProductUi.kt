package com.botsi.ai.ui.model

import com.android.billingclient.api.ProductDetails

data class BotsiAiProductUi(
    val botsiProductId: Int = 0,
    val sourceProductId: String = "",
    val isConsumable: Boolean = false,
    val basePlanId: String = "",
    val offerIds: List<String> = emptyList(),
    val productDetails: ProductDetails? = null
)
