package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiProductToggleStateContent(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductToggleStateContentLayout? = null
) : BotsiBlockContent

@Keep
internal data class BotsiProductToggleStateContentLayout(
    val layout: BotsiLayoutDirection? = null,
    val align: BotsiAlign? = null,
    val padding: List<Int>? = null,
    val spacing: Int? = null,
)