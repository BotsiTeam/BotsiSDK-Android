package com.competo.botsi.view.model.ui

import com.competo.botsi.view.model.content.BotsiLayoutContentModel

sealed interface BotsiPaywallUiState {
    data class Success(val content: BotsiLayoutContentModel) : BotsiPaywallUiState
    data object None : BotsiPaywallUiState
    data object Loading : BotsiPaywallUiState
    data class Error(val message: String) : BotsiPaywallUiState
}