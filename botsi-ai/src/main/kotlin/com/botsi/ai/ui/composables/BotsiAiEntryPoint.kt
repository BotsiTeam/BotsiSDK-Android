package com.botsi.ai.ui.composables

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.botsi.ai.di.BotsiAiDiManager
import com.botsi.ai.ui.delegate.BotsiAiDelegate
import com.botsi.ai.ui.model.BotsiAiUiAction
import com.botsi.ai.ui.model.BotsiAiUiSideEffect
import com.botsi.ai.ui.model.BotsiUiPaywallType

@Composable
fun BotsiAiEntryPoint(
    activity: Activity,
    placementId: String,
    secretKey: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val diManager = remember(Unit) { BotsiAiDiManager(context) }
    val delegate = diManager.inject<BotsiAiDelegate>()
    val state by delegate.state.collectAsState()
    val sideEffects by delegate.uiSideEffect.collectAsState(BotsiAiUiSideEffect.None())

    LaunchedEffect(Unit) {
        delegate.onAction(
            BotsiAiUiAction.Init(
                placementId = placementId,
                secretKey = secretKey
            )
        )
    }

    when {
        state.isSuccess -> {
            PaymentSuccessScreen(
                onBackClick = { delegate.onAction(BotsiAiUiAction.Back) }
            )
        }

        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        else -> {
            when (state.paywallType) {
                BotsiUiPaywallType.None -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Paywall")
                    }
                }

                BotsiUiPaywallType.First -> Paywall1Ui(
                    state = state,
                    activity = activity,
                    onAction = delegate::onAction,
                )

                BotsiUiPaywallType.Second -> Paywall2Ui(
                    state = state,
                    activity = activity,
                    onAction = delegate::onAction,
                )

                BotsiUiPaywallType.Third -> Paywall3Ui(
                    state = state,
                    activity = activity,
                    onAction = delegate::onAction,
                )
            }
        }
    }

    when (sideEffects) {
        is BotsiAiUiSideEffect.None -> {}
        is BotsiAiUiSideEffect.Back -> onBack()
    }

}