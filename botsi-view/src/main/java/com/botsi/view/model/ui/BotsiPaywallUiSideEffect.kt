package com.botsi.view.model.ui

internal sealed interface BotsiPaywallUiSideEffect {
    data object None : BotsiPaywallUiSideEffect
    data class Error(val message: String) : BotsiPaywallUiSideEffect
}