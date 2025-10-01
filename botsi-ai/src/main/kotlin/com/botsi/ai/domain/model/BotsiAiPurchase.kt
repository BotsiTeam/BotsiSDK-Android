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
        fun from(purchase: Purchase?): BotsiAiPurchase {
            return BotsiAiPurchase(
                purchaseToken = purchase?.purchaseToken.orEmpty(),
                purchaseTime = purchase?.purchaseTime ?: 0,
                products = purchase?.products.orEmpty(),
                purchaseState = purchase?.purchaseState ?: 0,
                orderId = purchase?.orderId,
                isAcknowledged = purchase?.isAcknowledged ?: false,
                isAutoRenewing = purchase?.isAutoRenewing ?: false,
                packageName = purchase?.packageName.orEmpty(),
                originalJson = purchase?.originalJson.orEmpty(),
                signature = purchase?.signature.orEmpty(),
            )
        }
    }
}
