package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiFooterContent(
    val padding: List<Int>? = null,
    val spacing: Int? = null,
    val style: BotsiFooterStyle? = null
) : BotsiBlockContent

@Keep
internal data class BotsiFooterStyle(
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Int? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val radius: List<Int>? = null
)