package com.botsi.view.model.ui

import com.botsi.view.model.content.BotsiPaywallContentStructure


internal sealed interface BotsiPaywallUiState {
    data class Success(val content: BotsiPaywallContentStructure) : BotsiPaywallUiState
    data object None : BotsiPaywallUiState
    data object Loading : BotsiPaywallUiState
    data class Error(val message: String) : BotsiPaywallUiState
}