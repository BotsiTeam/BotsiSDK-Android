package com.botsi.view.model.content

internal data class BotsiCarouselContent(
    val height: Int? = null,
    val pageControl: Boolean? = null,
    val style: BotsiCarouselStyle? = null,
    val slideShow: Boolean? = null,
    val timing: BotsiCarouselTiming? = null,
    val backgroundImage: String? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null,
    val contentPadding: List<Int>? = null,
    val spacing: Int? = null,
) : BotsiBlockContent

internal data class BotsiCarouselStyle(
    val activeColor: String? = null,
    val activeOpacity: Float? = null,
    val defaultColor: String? = null,
    val defaultOpacity: Float? = null,
    val size: Int? = null,
    val sizeOption: BotsiCarouselPageControlType? = null,
    val padding: List<Int>? = null,
    val spacing: Int? = null,
)

internal data class BotsiCarouselTiming(
    val timing: Long? = null,
    val initialTiming: Long? = null,
    val lastOption: BotsiCarouselLastOption? = null,
    val transition: Long? = null,
    val interactive: BotsiCarouselInteractive? = null,
)