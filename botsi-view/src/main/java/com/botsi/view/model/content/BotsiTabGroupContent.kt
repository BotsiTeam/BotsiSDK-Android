package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiTabGroupContent(
    val tabTitle: String? = null,
    val textFallback: String? = null,
    val selectedProduct: String? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductContentLayout? = null
) : BotsiBlockContent