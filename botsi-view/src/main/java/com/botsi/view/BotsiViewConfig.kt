package com.botsi.view

import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct

class BotsiViewConfig(
    val paywall: BotsiPaywall? = null,
    val products: List<BotsiProduct>? = null,
)

fun BotsiViewConfig.isNotEmpty(): Boolean = paywall != null