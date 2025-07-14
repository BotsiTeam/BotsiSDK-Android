package com.botsi.view.model.content

import androidx.annotation.Keep

@Keep
internal data class BotsiTabControlContent(
    val selectedTab: String? = null,
    val tabState: BotsiTabState? = null,
    val containerStyle: BotsiButtonStyle? = null,
    val activeState: BotsiTabControlState? = null,
    val inactiveState: BotsiTabControlState? = null,
    val tabTextFont: BotsiFont? = null,
    val tabTextSize: Int? = null,
    val padding: List<Int>? = null,
    val verticalOffset: Int? = null
) : BotsiBlockContent

@Keep
internal data class BotsiTabControlState(
    val stateStyle: BotsiButtonStyle? = null,
    val padding: List<Int>? = null,
    val fontColor: String? = null,
    val fontOpacity: Float? = null
)