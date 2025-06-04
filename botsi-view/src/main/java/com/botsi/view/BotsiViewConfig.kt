package com.botsi.view

class BotsiViewConfig(
    val paywallId: Long = 0,
    val placementId: String = "",
)

fun BotsiViewConfig.isNotEmpty(): Boolean = paywallId > 0 && placementId.isNotEmpty()