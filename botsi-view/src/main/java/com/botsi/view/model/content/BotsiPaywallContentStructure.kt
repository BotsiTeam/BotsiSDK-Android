package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiPaywallContentStructure(
    val layout: BotsiPaywallBlock? = null,
    val heroImage: BotsiPaywallBlock? = null,
    val content: List<BotsiPaywallBlock>? = null,
    val footer: BotsiPaywallBlock? = null,
)