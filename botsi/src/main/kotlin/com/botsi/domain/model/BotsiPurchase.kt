package com.botsi.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.Purchase

/**
 * Represents a completed purchase transaction from Google Play Billing.
 *
 * This class wraps the Google Play [Purchase] object and provides access to all
 * purchase-related information. It is returned by [com.botsi.Botsi.makePurchase]
 * when a purchase is successfully completed and verified.
 *
 * ## Usage Example
 * ```kotlin
 * Botsi.makePurchase(
 *     activity = this,
 *     product = selectedProduct,
 *     callback = { purchase ->
 *         println("Purchase completed!")
 *         println("Product IDs: ${purchase.products}")
 *         println("Purchase time: ${Date(purchase.purchaseTime)}")
 *         println("Order ID: ${purchase.orderId}")
 *         
 *         // Check purchase state
 *         when (purchase.purchaseState) {
 *             Purchase.PurchaseState.PURCHASED -> {
 *                 println("Purchase successful")
 *                 // Grant user access to content
 *             }
 *             Purchase.PurchaseState.PENDING -> {
 *                 println("Purchase pending")
 *                 // Handle pending purchase
 *             }
 *         }
 *         
 *         // Check if it's a subscription
 *         if (purchase.isAutoRenewing) {
 *             println("This is an auto-renewing subscription")
 *         }
 *     }
 * )
 * ```
 *
 * @param purchaseToken Unique token identifying this purchase, used for verification
 * @param purchaseTime The time when the purchase was made, in milliseconds since epoch
 * @param products List of product IDs that were purchased in this transaction
 * @param purchaseState The current state of the purchase (purchased, pending, etc.)
 * @param orderId The unique order identifier from Google Play, if available
 * @param isAcknowledged Whether this purchase has been acknowledged by the app
 * @param isAutoRenewing Whether this purchase will automatically renew (for subscriptions)
 * @param packageName The package name of the app that made the purchase
 * @param originalJson The original JSON response from Google Play Billing
 * @param signature The cryptographic signature for purchase verification
 * @since 1.0.0
 * @see com.botsi.Botsi.makePurchase
 * @see Purchase
 */
@Keep
data class BotsiPurchase(
    val purchaseToken: String,
    val purchaseTime: Long,
    val products: List<String>,
    val purchaseState: Int,
    val orderId: String?,
    val isAcknowledged: Boolean,
    val isAutoRenewing: Boolean,
    val packageName: String,
    val originalJson: String,
    val signature: String,
) {
    companion object {
        /**
         * Creates a [BotsiPurchase] from [Purchase]
         */
        fun from(purchase: Purchase?): BotsiPurchase? {
            return purchase?.let {
                BotsiPurchase(
                    purchaseToken = it.purchaseToken,
                    purchaseTime = it.purchaseTime,
                    products = it.products,
                    purchaseState = it.purchaseState,
                    orderId = it.orderId,
                    isAcknowledged = it.isAcknowledged,
                    isAutoRenewing = it.isAutoRenewing,
                    packageName = it.packageName,
                    originalJson = it.originalJson,
                    signature = it.signature,
                )
            }
        }
    }
}
