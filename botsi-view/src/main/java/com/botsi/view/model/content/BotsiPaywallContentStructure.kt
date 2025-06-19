package com.botsi.view.model.content

internal data class BotsiPaywallContentStructure(
    val layout: BotsiPaywallBlock? = null,
    val heroImage: BotsiPaywallBlock? = null,
    val content: List<BotsiPaywallBlock>? = null,
    val footer: BotsiPaywallBlock? = null,
)