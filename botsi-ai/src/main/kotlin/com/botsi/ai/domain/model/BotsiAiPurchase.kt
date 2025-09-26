package com.botsi.ai.domain.model

import androidx.annotation.Keep
import com.android.billingclient.api.Purchase

@Keep
data class BotsiAiPurchase(
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
         * Creates a [BotsiAiPurchase] from [Purchase]
         */
        fun from(purchase: Purchase?): BotsiAiPurchase? {
            return purchase?.let {
                BotsiAiPurchase(
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
