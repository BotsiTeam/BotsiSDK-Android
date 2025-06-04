package com.botsi.view.model.content

internal data class BotsiButtonContent(
    val action: BotsiButtonAction? = null,
    val actionLabel: String? = null,
    val text: BotsiText? = null,
    val secondaryText: BotsiText? = null,
    val style: BotsiButtonStyle? = null,
    val margin: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiButtonContentLayout? = null
) : BotsiBlockContent

internal data class BotsiButtonContentLayout(
    val padding: List<Int>? = null,
    val align: BotsiAlign? = null
)