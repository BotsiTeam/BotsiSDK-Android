package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiProductToggleContent(
    val state: Boolean? = null,
    val toggleColor: String? = null,
    val toggleOpacity: Float? = null,
    val toggleStyle: BotsiProductToggleStyle? = null,
    val activeState: BotsiProductToggleState? = null,
    val inactiveState: BotsiProductToggleState? = null,
    val padding: List<Int>? = null,
    val verticaOffset: Int? = null,
    val contentLayout: BotsiProductToggleContentLayout? = null,
): BotsiBlockContent

@Keep
internal data class BotsiProductToggleStyle(
    val color: String? = null,
    val opacity: Float? = null,
    val borderColor: String? = null,
    val borderOpacity: Float? = null,
    val borderThickness: Int? = null,
    val radius: List<Int>? = null,
)

@Keep
internal data class BotsiProductToggleState(
    val text: String? = null,
    val textStyle: BotsiText? = null,
    val secondaryText: String? = null,
    val secondaryTextStyle: BotsiText? = null,
)

@Keep
internal data class BotsiProductToggleContentLayout(
    val padding: List<Int>? = null,
    val spacing: Int? = null,
)