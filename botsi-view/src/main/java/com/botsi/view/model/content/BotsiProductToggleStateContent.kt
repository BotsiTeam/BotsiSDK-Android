package com.botsi.view.model.content

internal data class BotsiProductToggleStateContent(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductToggleStateContentLayout? = null
) : BotsiBlockContent

internal data class BotsiProductToggleStateContentLayout(
    val layout: BotsiLayoutDirection? = null,
    val align: BotsiAlign? = null,
    val padding: List<Int>? = null,
    val spacing: Int? = null,
)