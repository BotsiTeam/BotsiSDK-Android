package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiCardContent(
    val style: BotsiCardStyle? = null,
    val backgroundImage: String? = null,
    val margin: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiCardContentLayout? = null,
) : BotsiBlockContent

@Keep
internal data class BotsiCardStyle(
    val color: String? = null,
    val opacity: Float? = null,
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Int? = null,
    val radius: List<Int>? = null,
)

@Keep
internal data class BotsiCardContentLayout(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val align: BotsiAlign? = null,
)