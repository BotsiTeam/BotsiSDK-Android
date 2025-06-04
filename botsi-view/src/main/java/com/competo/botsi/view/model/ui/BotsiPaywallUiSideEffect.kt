package com.competo.botsi.view.model.ui

sealed interface BotsiPaywallUiSideEffect {
    data object None : BotsiPaywallUiSideEffect
    data class Error(val message: String) : BotsiPaywallUiSideEffect
}