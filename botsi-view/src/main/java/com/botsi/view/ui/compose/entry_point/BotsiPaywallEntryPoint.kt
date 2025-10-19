package com.botsi.view.ui.compose.entry_point

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.botsi.Botsi
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.view.BotsiViewConfig
import com.botsi.view.delegate.BotsiPaywallDelegate
import com.botsi.view.di.BotsiPaywallDIManager
import com.botsi.view.handler.BotsiActionHandler
import com.botsi.view.handler.BotsiActionType
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.isNotEmpty
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.timer.BotsiTimerResolver
import com.botsi.view.ui.compose.composable.BotsiPaywallScreenComposable
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
internal fun BotsiPaywallEntryPoint(
    activity: Activity,
    viewConfig: BotsiViewConfig,
    timerResolver: BotsiTimerResolver = BotsiTimerResolver.default,
    eventHandler: BotsiPublicEventHandler? = null,
) {
    val context = LocalContext.current
    val defaultClickHandler = object : BotsiActionHandler {
        override fun onButtonClick(actionType: BotsiActionType, actionId: String?, url: String?) {
            when (actionType) {
                BotsiActionType.Close -> {
                    eventHandler?.onCloseAction()
                }

                BotsiActionType.Login -> {
                    eventHandler?.onLoginAction()
                }

                BotsiActionType.Restore -> {
                    eventHandler?.onRestoreAction()
                }

                BotsiActionType.Custom -> {
                    onCustomAction(actionId.orEmpty())
                }

                BotsiActionType.Link -> {
                    // Handle link action
                    url?.let { onLinkClick(it) }
                }

                BotsiActionType.None -> {
                }
            }
        }

        override fun onTopButtonClick(actionType: BotsiActionType, actionId: String?) {
            when (actionType) {
                BotsiActionType.Close -> {
                    eventHandler?.onCloseAction()
                }

                else -> {
                    onButtonClick(actionType, actionId)
                }
            }
        }

        override fun onLinkClick(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onCustomAction(actionId: String, actionLabel: String?) {
            eventHandler?.onCustomAction(actionId, actionLabel)
        }

        override fun onSuccessPurchase(
            profile: BotsiProfile,
            purchase: BotsiPurchase
        ) {
            eventHandler?.onSuccessPurchase(
                profile,
                purchase
            )
        }

        override fun onErrorPurchase(error: Throwable) {
            eventHandler?.onErrorPurchase(error)
        }
    }

    val diManager =
        remember(viewConfig, eventHandler) {
            BotsiPaywallDIManager(
                activity = activity,
                timerResolver = timerResolver,
                clickHandler = defaultClickHandler,
            )
        }
    val delegate = remember(viewConfig, eventHandler) { diManager.inject<BotsiPaywallDelegate>() }
    val snackbarHostState = remember(Unit) { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by delegate.uiState.collectAsState()
    val uiSideEffect by delegate.uiSideEffect.collectAsState(BotsiPaywallUiSideEffect.None)

    LaunchedEffect(Unit) {
        delegate.onAction(BotsiPaywallUiAction.View)
    }

    LaunchedEffect(Unit) {
        viewConfig.paywall?.let {
            Botsi.logShowPaywall(it)
        }
    }

    val timerManager = diManager.inject<BotsiTimerManager>()

    if (viewConfig.isNotEmpty()) {
        LaunchedEffect(viewConfig) {
            viewConfig.paywall?.let {
                delegate.onAction(
                    BotsiPaywallUiAction.Load(
                        paywall = it,
                        products = viewConfig.products.orEmpty()
                    )
                )
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                timerManager.dispose(scope)
            }
        }

        BotsiPaywallScreenComposable(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            timerManager = timerManager,
            scope = scope,
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
