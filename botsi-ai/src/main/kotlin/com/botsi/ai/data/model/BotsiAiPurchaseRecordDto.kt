package com.botsi.ai.data.model

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient

@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
data class BotsiAiPurchaseRecordDto(
    val purchaseToken: String,
    val purchaseTime: Long,
    val products: List<String>,
    @BillingClient.ProductType val type: String,
)
