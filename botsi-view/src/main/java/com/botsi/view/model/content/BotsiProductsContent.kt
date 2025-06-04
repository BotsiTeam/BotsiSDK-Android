package com.botsi.view.model.content

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
    val padding: String? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductContentLayout? = null
) : BotsiBlockContent

internal data class BotsiProductStyle(
    val borderColor: String? = null,
    val borderOpacity: Int? = null,
    val borderThickness: Int? = null,
    val color: String? = null,
    val opacity: Int? = null,
    val radius: String? = null
)

data class BotsiProductContentLayout(
    val layout: String? = null,
    val align: String? = null,
    val padding: String? = null,
    val spacing: Int? = null
)