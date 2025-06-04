package com.competo.botsi.view.ui.compose.composable

import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.competo.botsi.view.model.ui.BotsiPaywallUiAction
import com.competo.botsi.view.model.ui.BotsiPaywallUiState

@Composable
@RestrictTo(RestrictTo.Scope.LIBRARY)
fun BotsiPaywallScreenComposable(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: BotsiPaywallUiState,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {

        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when (uiState) {
                is BotsiPaywallUiState.None -> {}
                is BotsiPaywallUiState.Loading -> {
                    Loader()
                }

                is BotsiPaywallUiState.Success -> {
                    Text(text = uiState.content.toString())
                }

                is BotsiPaywallUiState.Error -> {
                    Error(errorMessage = uiState.message)
                }
            }
        }
    }
}

@Composable
private fun Loader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(36.dp))
    }
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
    errorMessage: String,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(android.R.drawable.stat_notify_error),
                contentDescription = null,
                tint = Color.Red
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}