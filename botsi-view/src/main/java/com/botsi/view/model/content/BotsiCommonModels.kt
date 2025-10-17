package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiText(
    val text: String? = null,
    val font: BotsiFont? = null,
    val size: Float? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val align: BotsiAlign? = null,
    val style: BotsiComponentStyle? = null
)

@Keep
internal data class BotsiFont(
    val id: String? = null,
    val url: String? = null,
    val name: String? = null,
    val isSelected: Boolean? = null,
    val types: List<BotsiFontType>? = null
)

@Keep
internal data class BotsiFontType(
    val name: String? = null,
    val id: String? = null,
    val fontWeight: Int? = null,
    val fontStyle: BotsiFontStyleType? = null,
    val isSelected: Boolean? = null
)

@Keep
internal data class BotsiProductTextStyle(
    val font: BotsiFont? = null,
    val size: Int? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val selectedColor: String? = null,
    val selectedOpacity: Float? = null
)

@Keep
internal data class BotsiComponentStyle(
    val fillColor: BotsiColorBehaviour? = null,
    val color: String? = null,
    val opacity: Float? = null,
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Float? = null,
    val font: BotsiFont? = null,
    val size: Float? = null,
    val radius: List<Int>? = null,
)

@Keep
internal data class BotsiGradient(
    val degrees: Float? = null,
    val colors: List<BotsiGradientColor>? = null
): BotsiColorBehaviour

@Keep
internal data class BotsiGradientColor(
    val color: String? = null,
    val position: Float? = null,
)

@Keep
internal sealed interface BotsiColorBehaviour

@Keep
internal data class BotsiColor(
    val color: String
) : BotsiColorBehaviour
