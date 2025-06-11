package com.botsi.view

class BotsiViewConfig(
    val paywallId: Long = 0,
)

fun BotsiViewConfig.isNotEmpty(): Boolean = paywallId > 0