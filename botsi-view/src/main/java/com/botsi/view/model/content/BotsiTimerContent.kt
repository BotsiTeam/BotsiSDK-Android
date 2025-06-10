package com.botsi.view.model.content

import java.text.SimpleDateFormat

internal data class BotsiTimerContent(
    val dateFormat: SimpleDateFormat? = null,
    val noJavaFormatSupportSeparatorValues: List<String>? = null,
    val startTime: Long? = null,
    val beforeText: String? = null,
    val afterText: String? = null,
    val beforeTextFallback: String? = null,
    val afterTextFallback: String? = null,
    val style: BotsiText? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
) : BotsiBlockContent