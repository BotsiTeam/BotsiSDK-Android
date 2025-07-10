package com.botsi.domain.model

import androidx.annotation.Keep
import java.util.Date

@Keep
class BotsiProfile(
    val profileId: String,
    val customerUserId: String,
    val accessLevels: Map<String, AccessLevel>,
    val subscriptions: Map<String, Subscription>,
    val nonSubscriptions: Map<String, NonSubscription>,
    val custom: List<CustomEntry>,
) {

    @Keep
    data class AccessLevel(
        val createdDate: Date,
        val id: Int,
        val isActive: Boolean,
        val sourceProductId: String,
        val sourceBasePlanId: String,
        val store: String,
        val activatedAt: Date,
        val isLifetime: Boolean,
        val isRefund: Boolean,
        val willRenew: Boolean,
        val isInGracePeriod: Boolean,
        val cancellationReason: String,
        val offerId: String,
        val startsAt: Date,
        val renewedAt: Date,
        val expiresAt: Date,
        val activeIntroductoryOfferType: String,
        val activePromotionalOfferType: String,
        val activePromotionalOfferId: String,
        val unsubscribedAt: Date,
        val billingIssueDetectedAt: Date,
    )

    @Keep
    data class Subscription(
        val createdDate: Date,
        val id: Int,
        val isActive: Boolean,
        val sourceProductId: String,
        val sourceBasePlanId: String,
        val store: String,
        val activatedAt: Date,
        val isLifetime: Boolean,
        val isRefund: Boolean,
        val willRenew: Boolean,
        val isInGracePeriod: Boolean,
        val cancellationReason: String,
        val offerId: String,
        val startsAt: Date,
        val renewedAt: Date,
        val expiresAt: Date,
        val activeIntroductoryOfferType: String,
        val activePromotionalOfferType: String,
        val activePromotionalOfferId: String,
        val unsubscribedAt: Date,
        val billingIssueDetectedAt: Date,
    )

    @Keep
    data class NonSubscription(
        val isConsumable: Boolean,
        val isOneTime: Boolean,
        val isRefund: Boolean,
        val purchasedAt: Date,
        val purchasedId: String,
        val store: String,
        val sourceProductId: String,
        val transactionId: String,
    )

    @Keep
    data class CustomEntry(
        val key: String?,
        val value: String?,
        val id: String?,
    )

}
