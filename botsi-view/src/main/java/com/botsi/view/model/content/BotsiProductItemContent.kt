package com.botsi.view.model.content

internal data class BotsiProductItemContent(
    val state: String? = null,
    val offerState: String? = null,
    val defaultText: BotsiProductText? = null,
    val freeText: BotsiProductText? = null,
    val paygText: BotsiProductText? = null,
    val paufText: BotsiProductText? = null,
    val defaultState: BotsiProductStateStyle? = null,
    val selectedState: BotsiProductStateStyle? = null,
    val defaultStyle: BotsiProductStyle? = null,
    val selectedStyle: BotsiProductStyle? = null,
    val isBadge: Boolean? = null,
    val badge: BotsiBadge? = null
) : BotsiBlockContent

internal data class BotsiProductText(
    val text1: String? = null,
    val text1Fallback: String? = null,
    val text2: String? = null,
    val text2Fallback: String? = null,
    val text3: String? = null,
    val text3Fallback: String? = null,
    val text4: String? = null,
    val text4Fallback: String? = null
)

internal data class BotsiProductStateStyle(
    val text1: BotsiProductTextStyle? = null,
    val text2: BotsiProductTextStyle? = null,
    val text3: BotsiProductTextStyle? = null,
    val text4: BotsiProductTextStyle? = null
)

internal data class BotsiBadge(
    val badgeText: String? = null,
    val badgeColor: String? = null,
    val badgeOpacity: Int? = null,
    val badgeRadius: String? = null,
    val badgeTextFont: BotsiFont? = null,
    val badgeTextSize: String? = null,
    val badgeTextColor: String? = null,
    val badgeTextOpacity: Int? = null
)