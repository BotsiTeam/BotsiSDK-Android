package com.botsi.data.model.dto

import androidx.annotation.Keep
import com.android.billingclient.api.BillingClient
import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiPurchase

@Keep
internal data class BotsiSyncPurchaseDto(
    val purchaseToken: String,
    val purchaseTime: Long,
    val products: List<String>,
    @BillingClient.ProductType val type: String,
)
