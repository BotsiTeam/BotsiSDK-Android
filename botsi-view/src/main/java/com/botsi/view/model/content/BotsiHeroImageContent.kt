package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiHeroImageContent(
    val type: String? = null,
    val style: BotsiHeroImageContentStyle? = null,
    val backgroundImage: String? = null,
    val height: Float? = null,
    val shape: BotsiHeroImageShape? = null,
    val fillColor: BotsiColorBehaviour? = null,
    val layout: BotsiHeroLayout? = null
): BotsiBlockContent

@Keep
internal data class BotsiHeroLayout(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null
)
