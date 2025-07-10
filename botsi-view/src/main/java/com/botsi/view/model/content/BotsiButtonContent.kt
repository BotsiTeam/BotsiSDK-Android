package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
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

@Keep
internal data class BotsiButtonContentLayout(
    val padding: List<Int>? = null,
    val align: BotsiAlign? = null
)