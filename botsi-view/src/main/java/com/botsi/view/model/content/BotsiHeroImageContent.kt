package com.botsi.view.model.content

internal data class BotsiHeroImageContent(
    val type: String? = null,
    val style: BotsiHeroImageContentStyle? = null,
    val backgroundImage: String? = null,
    val height: Float? = null,
    val shape: BotsiShape? = null,
    val tint: BotsiTint? = null,
    val layout: BotsiHeroLayout? = null
): BotsiBlockContent

internal data class BotsiTint(
    val opacity: Float? = null,
    val fillColor: String? = null
)

internal data class BotsiHeroLayout(
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null
)