package com.botsi.data.model.dto

import com.android.billingclient.api.Purchase
import com.botsi.domain.model.BotsiPurchasableProduct

internal data class BotsiUnsyncPurchaseDto(
    val purchase: Purchase,
    val product: BotsiPurchasableProduct,
)