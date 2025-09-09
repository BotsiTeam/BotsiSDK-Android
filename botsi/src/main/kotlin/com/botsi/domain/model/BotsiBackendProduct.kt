package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiBackendProduct(
    val botsiProductId: Int,
    val productId: String,
    val paywallId: Long,
    val abTestId: Long,
    val placementId: String,
    val paywallName: String,
    val isConsumable: Boolean,
    val basePlanId: String,
    val offerIds: List<String>,
)