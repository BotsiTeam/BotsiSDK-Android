package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiProductsContent(
    val grouping: String? = null,
    val selectedProduct: String? = null,
    val state: String? = null,
    val defaultStyle: BotsiProductStyle? = null,
    val selectedStyle: BotsiProductStyle? = null,
    val text1: BotsiProductTextStyle? = null,
    val text2: BotsiProductTextStyle? = null,
    val text3: BotsiProductTextStyle? = null,
    val text4: BotsiProductTextStyle? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductContentLayout? = null
) : BotsiBlockContent

internal data class BotsiProductStyle(
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Int? = null,
    val color: BotsiColorBehaviour? = null,
    val opacity: Float? = null,
    val radius: List<Int>? = null
)

@Keep
internal data class BotsiProductContentLayout(
    val layout: BotsiLayoutDirection? = null,
    val align: BotsiAlign? = null,
    val padding: List<Int>? = null,
    val spacing: Int? = null
)