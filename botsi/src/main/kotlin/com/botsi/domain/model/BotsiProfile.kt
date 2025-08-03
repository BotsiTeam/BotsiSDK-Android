package com.botsi.domain.model

import androidx.annotation.Keep
import java.util.Date

/**
 * Represents a user profile in the Botsi system containing all user-related data.
 *
 * This class contains comprehensive information about a user including their subscription status,
 * purchase history, access levels, and custom attributes. It is returned by various SDK methods
 * such as [com.botsi.Botsi.getProfile], [com.botsi.Botsi.activate], and purchase operations.
 *
 * ## Usage Example
 * ```kotlin
 * Botsi.getProfile(
 *     successCallback = { profile ->
 *         println("User ID: ${profile.customerUserId}")
 *         println("Profile ID: ${profile.profileId}")
 *         
 *         // Check active subscriptions
 *         profile.subscriptions.values.forEach { subscription ->
 *             if (subscription.isActive) {
 *                 println("Active subscription: ${subscription.sourceProductId}")
 *             }
 *         }
 *         
 *         // Check access levels
 *         profile.accessLevels.values.forEach { accessLevel ->
 *             if (accessLevel.isActive) {
 *                 println("Active access: ${accessLevel.sourceProductId}")
 *             }
 *         }
 *     }
 * )
 * ```
 *
 * @param profileId The unique identifier for this profile in the Botsi system
 * @param customerUserId The customer user identifier, can be set via [com.botsi.Botsi.identify]
 * @param accessLevels Map of access levels keyed by access level identifier
 * @param subscriptions Map of subscription data keyed by subscription identifier
 * @param nonSubscriptions Map of one-time purchase data keyed by purchase identifier
 * @param custom List of custom attributes associated with this profile
 * @since 1.0.0
 */
@Keep
class BotsiProfile(
    val profileId: String,
    val customerUserId: String,
    val accessLevels: Map<String, AccessLevel>,
    val subscriptions: Map<String, Subscription>,
    val nonSubscriptions: Map<String, NonSubscription>,
    val custom: List<CustomEntry>,
) {

    /**
     * Represents an access level granted to the user.
     *
     * Access levels define what content or features a user can access based on their purchases.
     * They are derived from subscriptions and one-time purchases and provide a unified way
     * to check user entitlements.
     *
     * @param createdDate When this access level was first created
     * @param id Unique identifier for this access level
     * @param isActive Whether this access level is currently active and valid
     * @param sourceProductId The product ID that granted this access level
     * @param sourceBasePlanId The base plan ID for subscription products
     * @param store The store where the purchase was made (e.g., "google_play")
     * @param activatedAt When this access level was activated
     * @param isLifetime Whether this is a lifetime access (never expires)
     * @param isRefund Whether this access was refunded
     * @param willRenew Whether this access will automatically renew (for subscriptions)
     * @param isInGracePeriod Whether the subscription is in grace period after payment failure
     * @param cancellationReason Reason for cancellation if applicable
     * @param offerId The offer ID used for this purchase
     * @param startsAt When this access level starts being valid
     * @param renewedAt When this access was last renewed
     * @param expiresAt When this access level expires (if not lifetime)
     * @param activeIntroductoryOfferType Type of introductory offer currently active
     * @param activePromotionalOfferType Type of promotional offer currently active
     * @param activePromotionalOfferId ID of the promotional offer currently active
     * @param unsubscribedAt When the user unsubscribed (if applicable)
     * @param billingIssueDetectedAt When billing issues were detected (if any)
     * @since 1.0.0
     */
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

    /**
     * Represents a subscription purchase made by the user.
     *
     * Contains detailed information about subscription status, renewal behavior,
     * and billing information. This data is synchronized with Google Play Billing
     * and provides comprehensive subscription management capabilities.
     *
     * @param createdDate When this subscription record was first created
     * @param id Unique identifier for this subscription
     * @param isActive Whether this subscription is currently active
     * @param sourceProductId The Google Play product ID for this subscription
     * @param sourceBasePlanId The base plan ID for this subscription
     * @param store The store where the subscription was purchased
     * @param activatedAt When this subscription was first activated
     * @param isLifetime Whether this is a lifetime subscription (never expires)
     * @param isRefund Whether this subscription was refunded
     * @param willRenew Whether this subscription will automatically renew
     * @param isInGracePeriod Whether the subscription is in grace period after payment failure
     * @param cancellationReason Reason for cancellation if the subscription was cancelled
     * @param offerId The offer ID used for this subscription purchase
     * @param startsAt When this subscription period starts
     * @param renewedAt When this subscription was last renewed
     * @param expiresAt When this subscription expires
     * @param activeIntroductoryOfferType Type of introductory offer currently active
     * @param activePromotionalOfferType Type of promotional offer currently active
     * @param activePromotionalOfferId ID of the promotional offer currently active
     * @param unsubscribedAt When the user cancelled the subscription
     * @param billingIssueDetectedAt When billing issues were detected
     * @since 1.0.0
     */
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

    /**
     * Represents a one-time (non-subscription) purchase made by the user.
     *
     * Contains information about consumable and non-consumable in-app purchases.
     * These purchases do not renew automatically and are typically used for
     * permanent unlocks, consumable items, or one-time features.
     *
     * @param isConsumable Whether this purchase is consumable (can be purchased multiple times)
     * @param isOneTime Whether this is a one-time purchase (non-renewable)
     * @param isRefund Whether this purchase was refunded
     * @param purchasedAt When this purchase was made
     * @param purchasedId Unique identifier for this purchase
     * @param store The store where the purchase was made
     * @param sourceProductId The Google Play product ID for this purchase
     * @param transactionId The transaction ID from the store
     * @since 1.0.0
     */
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

    /**
     * Represents a custom attribute associated with the user profile.
     *
     * Custom entries allow storing additional user-specific data that can be
     * set via [com.botsi.Botsi.updateProfile] and retrieved with the profile.
     * These are useful for storing app-specific user preferences or metadata.
     *
     * @param key The key/name of the custom attribute
     * @param value The value of the custom attribute
     * @param id Unique identifier for this custom entry
     * @since 1.0.0
     */
    @Keep
    data class CustomEntry(
        val key: String?,
        val value: String?,
        val id: String?,
    )

}
