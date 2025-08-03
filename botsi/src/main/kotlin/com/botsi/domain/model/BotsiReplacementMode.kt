package com.botsi.domain.model

import androidx.annotation.Keep

/**
 * Defines how subscription replacements are handled when upgrading, downgrading, or changing subscription plans.
 *
 * These modes determine the billing behavior and timing when a user switches from one subscription
 * to another. Use these modes with [BotsiSubscriptionUpdateParameters] when calling
 * [com.botsi.Botsi.makePurchase] for subscription changes.
 *
 * ## Usage Example
 * ```kotlin
 * val updateParams = BotsiSubscriptionUpdateParameters(
 *     oldSubVendorProductId = "basic_monthly",
 *     replacementMode = BotsiReplacementMode.WITH_TIME_PRORATION
 * )
 * 
 * Botsi.makePurchase(
 *     activity = this,
 *     product = premiumProduct,
 *     subscriptionUpdateParams = updateParams,
 *     callback = { purchase -> /* handle success */ }
 * )
 * ```
 *
 * @since 1.0.0
 * @see BotsiSubscriptionUpdateParameters
 * @see com.botsi.Botsi.makePurchase
 */
@Keep
enum class BotsiReplacementMode {

    /**
     * The user receives credit for unused time from the old subscription.
     * 
     * The new subscription starts immediately, and the user is credited for the unused
     * portion of their current subscription period. This is typically used for upgrades
     * where you want to give the user immediate access while being fair about billing.
     */
    WITH_TIME_PRORATION,

    /**
     * The user is charged the prorated price difference immediately.
     * 
     * The new subscription starts immediately, and the user pays the difference between
     * the new and old subscription prices, prorated for the remaining time in the current
     * billing period. This is commonly used for mid-cycle upgrades.
     */
    CHARGE_PRORATED_PRICE,

    /**
     * The new subscription starts immediately without any proration.
     * 
     * The user is not credited for unused time from the old subscription, and the new
     * subscription begins immediately. The user will be charged the full price of the
     * new subscription at the next billing cycle.
     */
    WITHOUT_PRORATION,

    /**
     * The new subscription starts when the current subscription expires.
     * 
     * The current subscription continues until its natural expiration date, and the
     * new subscription begins at that time. This is useful for downgrades where you
     * want to honor the current subscription period.
     */
    DEFERRED,

    /**
     * The user is charged the full price of the new subscription immediately.
     * 
     * The new subscription starts immediately, and the user pays the full price
     * regardless of any unused time from the old subscription. This mode should
     * be used carefully as it may result in double-charging.
     */
    CHARGE_FULL_PRICE,
}
