package com.botsi.view.model.content

internal data class BotsiImageContent(
    val image: String? = null,
    val height: Int? = null,
    val aspect: BotsiImageAspect? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
) : BotsiBlockContent