package com.botsi.domain.model

import androidx.annotation.Keep

/**
 * Parameters for updating, upgrading, or downgrading a subscription.
 *
 * This class contains the necessary parameters to perform subscription changes
 * such as upgrading to a higher tier, downgrading to a lower tier, or switching
 * between different subscription plans. Use this class with [com.botsi.Botsi.makePurchase]
 * when the `subscriptionUpdateParams` parameter is provided.
 *
 * ## Usage Example
 * ```kotlin
 * // Upgrade from basic to premium subscription
 * val updateParams = BotsiSubscriptionUpdateParameters(
 *     oldSubVendorProductId = "basic_monthly_subscription",
 *     replacementMode = BotsiReplacementMode.WITH_TIME_PRORATION
 * )
 * 
 * Botsi.makePurchase(
 *     activity = this,
 *     product = premiumSubscriptionProduct,
 *     subscriptionUpdateParams = updateParams,
 *     callback = { purchase ->
 *         println("Subscription upgraded successfully!")
 *         println("New subscription: ${purchase.products}")
 *     },
 *     errorCallback = { error ->
 *         Log.e("Botsi", "Failed to upgrade subscription", error)
 *     }
 * )
 * ```
 *
 * ## Replacement Modes
 * - `WITHOUT_PRORATION`: The new subscription starts immediately, but the user is not credited for unused time
 * - `WITH_TIME_PRORATION`: The user receives credit for unused time from the old subscription
 * - `CHARGE_PRORATED_PRICE`: The user is charged the prorated price difference immediately
 * - `CHARGE_FULL_PRICE`: The user is charged the full price of the new subscription immediately
 * - `DEFERRED`: The new subscription starts when the current subscription expires
 *
 * @param oldSubVendorProductId The product ID of the current subscription to be replaced
 * @param replacementMode The mode to use when replacing the subscription, affecting billing and timing
 * @since 1.0.0
 * @see com.botsi.Botsi.makePurchase
 * @see BotsiReplacementMode
 */
@Keep
data class BotsiSubscriptionUpdateParameters(
    val oldSubVendorProductId: String,
    val replacementMode: BotsiReplacementMode = BotsiReplacementMode.WITHOUT_PRORATION,
)
