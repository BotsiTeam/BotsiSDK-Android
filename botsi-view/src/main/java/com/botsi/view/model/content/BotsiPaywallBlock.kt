package com.botsi.view.model.content

internal data class BotsiPaywallBlock(
    val meta: BotsiBlockMeta? = null,
    val content: BotsiBlockContent? = null,
    val children: List<BotsiPaywallBlock>? = null
)

internal data class BotsiBlockMeta(
    val blockName: String? = null,
    val id: String? = null,
    val type: BotsiContentType? = null,
    val icon: String? = null,
    val parentId: String? = null,
    val offerType: String? = null,
)