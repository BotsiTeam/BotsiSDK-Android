package com.botsi.view.model.content

internal data class BotsiTimerContent(
    val format: BotsiTimerFormat? = null,
    val separator: BotsiTimerSeparator? = null,
    val startText: String? = null,
    val beforeText: String? = null,
    val afterText: String? = null,
    val beforeTextFallback: String? = null,
    val afterTextFallback: String? = null,
    val style: BotsiText? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
) : BotsiBlockContent