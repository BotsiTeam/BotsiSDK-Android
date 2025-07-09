package com.botsi.data.model.dto

import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiPurchase

internal data class BotsiUnsyncPurchaseDto(
    val purchase: BotsiPurchase,
    val product: BotsiPurchasableProduct,
)
