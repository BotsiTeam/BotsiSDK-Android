package com.botsi.domain.model

import androidx.annotation.Keep

/**
 * Represents a product available for purchase through the Botsi SDK.
 *
 * This class contains comprehensive information about a product including its pricing,
 * metadata, and purchase options. Products are obtained from [com.botsi.Botsi.getPaywallProducts]
 * and are used with [com.botsi.Botsi.makePurchase] to initiate purchase flows.
 *
 * ## Usage Example
 * ```kotlin
 * Botsi.getPaywallProducts(paywall) { products ->
 *     products.forEach { product ->
 *         println("Product: ${product.title}")
 *         println("Description: ${product.description}")
 *         println("Price: ${product.subscriptionOffer?.pricingPhases?.firstOrNull()?.formattedPrice}")
 *
 *         // Check if it's a subscription or one-time purchase
 *         if (product.subscriptionOffer != null) {
 *             println("This is a subscription product")
 *         } else if (product.onTimePurchaseOffers != null) {
 *             println("This is a one-time purchase")
 *         }
 *     }
 * }
 * ```
 *
 * @param productId The Google Play product identifier for this product
 * @param paywallId The unique identifier of the paywall this product belongs to
 * @param abTestId The A/B test identifier for analytics and experimentation
 * @param placementId The placement identifier where this product is displayed
 * @param paywallName The human-readable name of the paywall
 * @param type The type of product (e.g., "subscription", "consumable", "non_consumable")
 * @param name The internal name of the product
 * @param title The display title of the product shown to users
 * @param description The description of the product shown to users
 * @param isConsumable Whether this product is consumable (can be purchased multiple times)
 * @param basePlanId The base plan identifier for subscription products
 * @param subscriptionOffer The subscription offer details if this is a subscription product
 * @param onTimePurchaseOffers The one-time purchase offer details if this is a one-time product
 * @since 1.0.0
 * @see com.botsi.Botsi.getPaywallProducts
 * @see com.botsi.Botsi.makePurchase
 * @see BotsiSubscriptionOfferDetails
 * @see BotsiOneTimePurchaseOfferDetails
 */
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

@Keep
enum class BotsiSubscriptionRenewalType {
    Prepaid,
    Autorenewable,
}
