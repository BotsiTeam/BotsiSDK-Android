package com.botsi.data.model.dto

import androidx.annotation.RestrictTo
import com.android.billingclient.api.BillingClient

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal data class BotsiPurchaseRecordDto(
    val purchaseToken: String,
    val purchaseTime: Long,
    val products: List<String>,
    @BillingClient.ProductType val type: String,
)