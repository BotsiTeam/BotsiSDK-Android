package com.botsi.view.ui.compose.entry_point

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.botsi.view.BotsiViewConfig
import com.botsi.view.delegate.BotsiPaywallDelegate
import com.botsi.view.di.BotsiPaywallDIManager
import com.botsi.view.isNotEmpty
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.ui.compose.composable.BotsiPaywallScreenComposable
import kotlinx.coroutines.launch

@Composable
fun BotsiPaywallEntryPoint(viewConfig: BotsiViewConfig) {
    val diManager = remember(viewConfig) { BotsiPaywallDIManager() }
    val delegate = remember(viewConfig) { diManager.inject<BotsiPaywallDelegate>() }
    val snackbarHostState = remember(Unit) { SnackbarHostState() }

    val uiState by delegate.uiState.collectAsState()
    val uiSideEffect by delegate.uiSideEffect.collectAsState(BotsiPaywallUiSideEffect.None)

    LaunchedEffect(Unit) {
        delegate.onAction(BotsiPaywallUiAction.View)
    }

    if (viewConfig.isNotEmpty()) {
        LaunchedEffect(viewConfig) {
            delegate.onAction(
                BotsiPaywallUiAction.Load(
                    viewConfig.paywallId,
                )
            )
        }

        BotsiPaywallScreenComposable(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onAction = delegate::onAction
        )
    }

    BotsiCollectUiSideEffect(
        uiSideEffect = uiSideEffect,
        snackbarHostState = snackbarHostState,
        onAction = delegate::onAction,
    )
}

@Composable
private fun BotsiCollectUiSideEffect(
    uiSideEffect: BotsiPaywallUiSideEffect,
    snackbarHostState: SnackbarHostState,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (uiSideEffect) {
        is BotsiPaywallUiSideEffect.None -> {}
        is BotsiPaywallUiSideEffect.Error -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(uiSideEffect.message)
                    if (result == SnackbarResult.Dismissed) {
                        onAction(BotsiPaywallUiAction.None)
                    }
                }
            }
        }
    }
}