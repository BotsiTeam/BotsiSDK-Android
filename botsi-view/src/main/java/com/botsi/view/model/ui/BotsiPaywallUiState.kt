package com.botsi.view.model.ui

import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.model.content.BotsiPaywallContentStructure


internal sealed interface BotsiPaywallUiState {
    data class Success(
        val content: BotsiPaywallContentStructure,
        val paywall: BotsiPaywall,
        val products: List<BotsiProduct>,
        val selectedProductId: Long? = null,
    ) : BotsiPaywallUiState

    data object None : BotsiPaywallUiState
    data object Loading : BotsiPaywallUiState
    data class Error(val message: String) : BotsiPaywallUiState
}