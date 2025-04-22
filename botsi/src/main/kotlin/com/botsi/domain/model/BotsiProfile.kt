package com.botsi.domain.model

import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@RestrictTo(RestrictTo.Scope.LIBRARY)
class BotsiProfile(
    val profileId: String,
    val customerUserId: String,
    val birthday: String,
    val email: String,
    val userName: String,
    val gender: String,
    val phone: String,
    val state: String,
    val country: String,
    val device: String,
    val os: String,
    val platform: String,
    val advertisingId: String,
    val appBuild: String,
    val appVersion: String,
    val botsiSdkVersion: String,
    val idfa: String,
    val locale: String,
    val ip: String,
    val lastSeen: Long,
    val accessLevels: Map<String, AccessLevel>,
    val subscriptions: Map<String, Subscription>,
    val nonSubscriptions: Map<String, NonSubscription>,
    val custom: List<CustomEntry>,
) {

    data class AccessLevel(
        val createdDate: String,
        val id: Int,
        val isActive: Boolean,
        val sourceProductId: String,
        val sourceBasePlanId: String,
        val store: String,
        val activatedAt: String,
        val isLifetime: Boolean,
        val isRefund: Boolean,
        val willRenew: Boolean,
        val isInGracePeriod: Boolean,
        val cancellationReason: String,
        val offerId: String,
        val startsAt: String,
        val renewedAt: String,
        val expiresAt: String,
        val activeIntroductoryOfferType: String,
        val activePromotionalOfferType: String,
        val activePromotionalOfferId: String,
        val unsubscribedAt: String,
        val billingIssueDetectedAt: String,
    )

    data class Subscription(
        val createdDate: String,
        val id: Int,
        val isActive: Boolean,
        val sourceProductId: String,
        val sourceBasePlanId: String,
        val store: String,
        val activatedAt: String,
        val isLifetime: Boolean,
        val isRefund: Boolean,
        val willRenew: Boolean,
        val isInGracePeriod: Boolean,
        val cancellationReason: String,
        val offerId: String,
        val startsAt: String,
        val renewedAt: String,
        val expiresAt: String,
        val activeIntroductoryOfferType: String,
        val activePromotionalOfferType: String,
        val activePromotionalOfferId: String,
        val unsubscribedAt: String,
        val billingIssueDetectedAt: String,
    )

    data class NonSubscription(
        val isConsumable: Boolean,
        val isOneTime: Boolean,
        val isRefund: Boolean,
        val purchasedAt: String,
        val purchasedId: String,
        val store: String,
        val sourceProductId: String,
        val transactionId: String,
    )

    data class CustomEntry(
        val key: String?,
        val value: String?,
        val id: String?,
    )

}