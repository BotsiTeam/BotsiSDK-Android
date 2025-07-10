package com.botsi.domain.model

import androidx.annotation.Keep

@Keep
data class BotsiProduct(
    val productId: String,
    val paywallId: Long,
    val abTestId: Long,
    val placementId: String,
    val paywallName: String,
    val type: String,
    val name: String,
    val title: String,
    val description: String,
    val isConsumable: Boolean,
    val basePlanId: String,
    val subscriptionOffer: BotsiSubscriptionOfferDetails?,
    val onTimePurchaseOffers: BotsiOneTimePurchaseOfferDetails?,
)
