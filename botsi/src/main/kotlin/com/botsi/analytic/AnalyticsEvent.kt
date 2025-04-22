package com.botsi.analytic

import androidx.annotation.RestrictTo

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal data class AnalyticsEvent(
    val profileId: String? = null,
    val eventType: String? = null,
    val paywallId: String? = null,
    val timestamp: Long? = null,
    val placementId: String? = null,
    val abTestId: Long? = null,
    val productDuration: String? = null,
    val productId: String? = null,
    val country: String? = null,
    val store: String? = null,
    val offerType: String? = null,
    val offerCategory: String? = null,
    val offerId: String? = null,
    val transactionId: String? = null,
    val originalTransactionId: String? = null,
    val revenueUsd: Double? = null,
    val proceedsUsd: Double? = null,
    val revenueLocal: Double? = null,
    val proceedsLocal: Double? = null,
    val purchaseCurrency: String? = null,
    val cancellationReason: String? = null,
    val subscriptionExpiresAt: String? = null
)