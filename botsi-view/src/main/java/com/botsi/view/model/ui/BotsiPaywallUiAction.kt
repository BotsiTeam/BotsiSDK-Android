package com.botsi.view.model.ui

import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.model.content.BotsiButtonAction

internal sealed interface BotsiPaywallUiAction {
    data class Load(
        val paywall: BotsiPaywall,
        val products: List<BotsiProduct>,
    ) : BotsiPaywallUiAction

    data object None : BotsiPaywallUiAction
    data object View : BotsiPaywallUiAction
    data object Dispose : BotsiPaywallUiAction

    // Click actions
    data class ButtonClick(
        val action: BotsiButtonAction
    ) : BotsiPaywallUiAction

    data class TimerEnd(
        val customActionId: String
    ) : BotsiPaywallUiAction

    data class ProductSelected(
        val productId: Long?,
    ) : BotsiPaywallUiAction
}
