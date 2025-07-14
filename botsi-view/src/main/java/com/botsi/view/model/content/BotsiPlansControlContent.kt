package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiPlansControlContent(
    val state: Boolean? = null,
    val defaultText: BotsiPlansControlText? = null,
    val morePlansShownText: BotsiPlansControlText? = null,
    val style: BotsiButtonStyle? = null,
    val margin: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiPlansControlContentLayout? = null
) : BotsiBlockContent

@Keep
internal data class BotsiPlansControlText(
    val text: String? = null,
    val textFallback: String? = null,
    val textStyle: BotsiText? = null,
    val secondaryText: String? = null,
    val secondaryTextFallback: String? = null,
    val secondaryTextStyle: BotsiText? = null
)

@Keep
internal data class BotsiPlansControlContentLayout(
    val align: BotsiAlign? = null,
    val padding: List<Int>? = null
)