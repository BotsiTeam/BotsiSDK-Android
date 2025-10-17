package com.botsi.view.model.ui

import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.content.BotsiTopButton

internal sealed interface BotsiPaywallUiAction {
    data class Load(
        val paywallId: Long,
    ) : BotsiPaywallUiAction
    data object None : BotsiPaywallUiAction
    data object View : BotsiPaywallUiAction
    data object Dispose : BotsiPaywallUiAction
    // Click actions
    data class ButtonClick(
        val action: BotsiButtonAction,
        val actionId: String? = null
    ) : BotsiPaywallUiAction

    data class TopButtonClick(
        val topButton: BotsiTopButton
    ) : BotsiPaywallUiAction

    data class LinkClick(
        val url: String
    ) : BotsiPaywallUiAction

    data class CustomAction(
        val actionId: String,
        val actionLabel: String? = null
    ) : BotsiPaywallUiAction
}
