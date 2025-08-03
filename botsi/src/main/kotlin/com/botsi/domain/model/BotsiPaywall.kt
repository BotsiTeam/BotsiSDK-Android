package com.botsi.domain.model

import androidx.annotation.Keep

/**
 * Represents a paywall configuration in the Botsi system.
 *
 * A paywall is a monetization screen that displays products to users for purchase.
 * This class contains the configuration and metadata for a paywall, including
 * the products it contains and any remote configuration data. Paywalls are
 * obtained from [com.botsi.Botsi.getPaywall] and used with [com.botsi.Botsi.getPaywallProducts]
 * to retrieve products with pricing information.
 *
 * ## Usage Example
 * ```kotlin
 * Botsi.getPaywall(
 *     placementId = "premium_upgrade",
 *     successCallback = { paywall ->
 *         println("Paywall: ${paywall.name}")
 *         println("Placement: ${paywall.placementId}")
 *         println("Products: ${paywall.sourceProducts.size}")
 *         
 *         // Get products with Google Play pricing
 *         Botsi.getPaywallProducts(paywall) { products ->
 *             // Display paywall with products
 *             displayPaywall(paywall, products)
 *         }
 *         
 *         // Log paywall impression for analytics
 *         Botsi.logShowPaywall(paywall)
 *     }
 * )
 * ```
 *
 * @param id The unique identifier for this paywall in the Botsi system
 * @param placementId The placement identifier where this paywall is displayed
 * @param name The human-readable name of the paywall
 * @param abTestId The A/B test identifier for analytics and experimentation
 * @param remoteConfigs JSON string containing remote configuration data for the paywall
 * @param revision The revision number of this paywall configuration
 * @param sourceProducts List of backend product configurations associated with this paywall
 * @since 1.0.0
 * @see com.botsi.Botsi.getPaywall
 * @see com.botsi.Botsi.getPaywallProducts
 * @see com.botsi.Botsi.logShowPaywall
 * @see BotsiBackendProduct
 */
@Keep
data class BotsiPaywall(
    val id: Long,
    val placementId: String,
    val name: String,
    val abTestId: Long,
    val remoteConfigs: String,
    val revision: Long,
    val sourceProducts: List<BotsiBackendProduct>,
)
