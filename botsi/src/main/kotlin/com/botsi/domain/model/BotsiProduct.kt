package com.botsi.domain.model

import com.android.billingclient.api.ProductDetails

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
    val subscriptionOffers: List<ProductDetails.SubscriptionOfferDetails>?,
    val onTimePurchaseOffers: ProductDetails.OneTimePurchaseOfferDetails?,
)