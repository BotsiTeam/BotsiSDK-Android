package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiMorePlansSheetContent(
    val selectedProduct: String? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductContentLayout? = null,
    val plansStyles: BotsiButtonStyle? = null,
    val closeButtonStyles: BotsiButtonStyle? = null,
    val iconColor: String? = null,
    val iconOpacity: Int? = null,
    val iconSize: String? = null,
    val titleText: String? = null,
    val secondaryText: String? = null,
    val titleTextStyle: BotsiText? = null,
    val secondaryTextStyle: BotsiText? = null
) : BotsiBlockContent