package com.botsi.data.model.dto

import androidx.annotation.Keep
import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiPurchase

@Keep
internal data class BotsiUnsyncPurchaseDto(
    val purchase: BotsiPurchase,
    val product: BotsiPurchasableProduct,
)
