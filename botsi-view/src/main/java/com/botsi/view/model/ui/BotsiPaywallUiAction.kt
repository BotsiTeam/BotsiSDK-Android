package com.botsi.view.model.ui

sealed interface BotsiPaywallUiAction {
    data class Load(
        val paywallId: Long,
        val placementId: String
    ) : BotsiPaywallUiAction
    data object None : BotsiPaywallUiAction
    data object View : BotsiPaywallUiAction
    data object Dispose : BotsiPaywallUiAction
}