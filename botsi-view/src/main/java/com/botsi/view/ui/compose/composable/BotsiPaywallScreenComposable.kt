package com.botsi.view.ui.compose.composable

import android.R
import androidx.annotation.RestrictTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiLayoutContent
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiState
import com.botsi.view.utils.toArrangement
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings

@Composable
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiPaywallScreenComposable(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: BotsiPaywallUiState,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val contentLayout = remember(uiState) {
        (uiState as? BotsiPaywallUiState.Success)?.content?.layout?.content as? BotsiLayoutContent
    }

    Scaffold(
        modifier = modifier,
        containerColor = contentLayout?.backgroundColor.toColor(),
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            (uiState as? BotsiPaywallUiState.Success)?.content?.footer?.let {
                BotsiFooterComposable(footerBlock = it)
            }
        }
    ) {
        when (uiState) {
            is BotsiPaywallUiState.None -> {}
            is BotsiPaywallUiState.Loading -> {
                Loader()
            }

            is BotsiPaywallUiState.Success -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    contentLayout?.topButtons
                        ?.let {
                            Box(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                it.forEach { button ->
                                    BotsiTopButtonComposable(
                                        topButton = button,
                                        topButtonClick = {}
                                    )
                                }
                            }
                        }
                    LazyColumn(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxSize()
                            .padding(contentLayout?.contentLayout.toPaddings()),
                        verticalArrangement = contentLayout?.contentLayout.toArrangement()
                    ) {
                        BotsiContentComposable(
                            children = uiState.content.content.orEmpty()
                        )
                    }
                }
            }

            is BotsiPaywallUiState.Error -> {
                Error(errorMessage = uiState.message)
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
                painter = painterResource(R.drawable.stat_notify_error),
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