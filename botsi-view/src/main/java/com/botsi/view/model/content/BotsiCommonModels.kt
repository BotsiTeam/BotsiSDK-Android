package com.botsi.view.model.content

internal data class BotsiText(
    val text: String? = null,
    val font: BotsiFont? = null,
    val size: Float? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val align: BotsiAlign? = null
)

internal data class BotsiFont(
    val id: String? = null,
    val name: String? = null,
    val isSelected: Boolean? = null,
    val types: List<BotsiFontType>? = null
)

internal data class BotsiFontType(
    val name: String? = null,
    val id: String? = null,
    val fontWeight: Int? = null,
    val fontStyle: BotsiFontStyleType? = null,
    val isSelected: Boolean? = null
)

internal data class BotsiProductTextStyle(
    val font: BotsiFont? = null,
    val size: Int? = null,
    val color: String? = null,
    val opacity: Int? = null,
    val selectedColor: String? = null,
    val selectedOpacity: Int? = null
)


internal data class BotsiButtonStyle(
    val color: String? = null,
    val opacity: Float? = null,
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Float? = null,
    val radius: List<Int>? = null
)