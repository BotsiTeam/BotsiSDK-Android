package com.botsi.data.model.dto

import androidx.annotation.RestrictTo
import com.google.gson.annotations.SerializedName

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiProfileDto(
    @SerializedName("profileId") val profileId: String? = null,
    @SerializedName("customerUserId") val customerUserId: String? = null,
    @SerializedName("accessLevels") val accessLevels: Map<String, AccessLevelDto>? = null,
    @SerializedName("subscriptions") val subscriptions: Map<String, SubscriptionDto>? = null,
    @SerializedName("nonSubscriptions") val nonSubscriptions: Map<String, NonSubscriptionDto>? = null,
    @SerializedName("custom") val custom: List<CustomEntryDto>? = null,
) {

    data class AccessLevelDto(
        @SerializedName("createdDate") val createdDate: String? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("isActive") val isActive: Boolean? = null,
        @SerializedName("sourceProductId") val sourceProductId: String? = null,
        @SerializedName("sourceBasePlanId") val sourceBasePlanId: String? = null,
        @SerializedName("store") val store: String? = null,
        @SerializedName("activatedAt") val activatedAt: String? = null,
        @SerializedName("isLifetime") val isLifetime: Boolean? = null,
        @SerializedName("isRefund") val isRefund: Boolean? = null,
        @SerializedName("willRenew") val willRenew: Boolean? = null,
        @SerializedName("isInGracePeriod") val isInGracePeriod: Boolean? = null,
        @SerializedName("cancellationReason") val cancellationReason: String? = null,
        @SerializedName("offerId") val offerId: String? = null,
        @SerializedName("startsAt") val startsAt: String? = null,
        @SerializedName("renewedAt") val renewedAt: String? = null,
        @SerializedName("expiresAt") val expiresAt: String? = null,
        @SerializedName("activeIntroductoryOfferType") val activeIntroductoryOfferType: String? = null,
        @SerializedName("activePromotionalOfferType") val activePromotionalOfferType: String? = null,
        @SerializedName("activePromotionalOfferId") val activePromotionalOfferId: String? = null,
        @SerializedName("unsubscribedAt") val unsubscribedAt: String? = null,
        @SerializedName("billingIssueDetectedAt") val billingIssueDetectedAt: String? = null,
    )

    data class SubscriptionDto(
        @SerializedName("createdDate") val createdDate: String? = null,
        @SerializedName("id") val id: Int? = null,
        @SerializedName("isActive") val isActive: Boolean? = null,
        @SerializedName("sourceProductId") val sourceProductId: String? = null,
        @SerializedName("sourceBasePlanId") val sourceBasePlanId: String? = null,
        @SerializedName("store") val store: String? = null,
        @SerializedName("activatedAt") val activatedAt: String? = null,
        @SerializedName("isLifetime") val isLifetime: Boolean? = null,
        @SerializedName("isRefund") val isRefund: Boolean? = null,
        @SerializedName("willRenew") val willRenew: Boolean? = null,
        @SerializedName("isInGracePeriod") val isInGracePeriod: Boolean? = null,
        @SerializedName("cancellationReason") val cancellationReason: String? = null,
        @SerializedName("offerId") val offerId: String? = null,
        @SerializedName("startsAt") val startsAt: String? = null,
        @SerializedName("renewedAt") val renewedAt: String? = null,
        @SerializedName("expiresAt") val expiresAt: String? = null,
        @SerializedName("activeIntroductoryOfferType") val activeIntroductoryOfferType: String? = null,
        @SerializedName("activePromotionalOfferType") val activePromotionalOfferType: String? = null,
        @SerializedName("activePromotionalOfferId") val activePromotionalOfferId: String? = null,
        @SerializedName("unsubscribedAt") val unsubscribedAt: String? = null,
        @SerializedName("billingIssueDetectedAt") val billingIssueDetectedAt: String? = null,
    )

    data class NonSubscriptionDto(
        @SerializedName("isConsumable") val isConsumable: Boolean? = null,
        @SerializedName("isOneTime") val isOneTime: Boolean? = null,
        @SerializedName("isRefund") val isRefund: Boolean? = null,
        @SerializedName("purchasedAt") val purchasedAt: String? = null,
        @SerializedName("purchasedId") val purchasedId: String? = null,
        @SerializedName("store") val store: String? = null,
        @SerializedName("sourceProductId") val sourceProductId: String? = null,
        @SerializedName("transactionId") val transactionId: String? = null,
    )

    data class CustomEntryDto(
        @SerializedName("key") val key: String? = null,
        @SerializedName("value") val value: String? = null,
        @SerializedName("id") val id: String? = null,
    )
}