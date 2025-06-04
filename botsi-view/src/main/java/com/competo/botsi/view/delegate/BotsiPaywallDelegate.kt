package com.competo.botsi.view.delegate

import com.competo.botsi.view.model.ui.BotsiPaywallUiAction
import com.competo.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.competo.botsi.view.model.ui.BotsiPaywallUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BotsiPaywallDelegate {
    val uiState: StateFlow<BotsiPaywallUiState>
    val uiSideEffect: Flow<BotsiPaywallUiSideEffect>
    fun onAction(action: BotsiPaywallUiAction)
}