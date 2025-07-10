package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiTextContent(
    val text: BotsiText? = null,
    val maxLines: Int? = null,
    val onOverflow: BotsiOnOverflowBehavior? = null,
    val margin: List<Int>? = null,
    val verticalOffset: Int? = null
) : BotsiBlockContent