package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiPlansContent(
    val selectedProduct: String? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentLayout: BotsiProductContentLayout? = null
) : BotsiBlockContent