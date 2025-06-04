package com.botsi.view.delegate

import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.model.ui.BotsiPaywallUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface BotsiPaywallDelegate {
    val uiState: StateFlow<BotsiPaywallUiState>
    val uiSideEffect: Flow<BotsiPaywallUiSideEffect>
    fun onAction(action: BotsiPaywallUiAction)
}