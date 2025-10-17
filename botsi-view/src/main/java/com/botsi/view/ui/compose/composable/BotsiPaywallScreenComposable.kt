package com.botsi.view.ui.compose.composable

import android.R
import androidx.annotation.RestrictTo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.model.content.BotsiHeroImageContentStyle
import com.botsi.view.model.content.BotsiLayoutContent
import com.botsi.view.model.content.BotsiPaywallContentStructure
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiState
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.ui.compose.scroll.BotsiHeroImageOverlayNestedScroll
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toBrush
import com.botsi.view.utils.toImageHeightPx
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape
import kotlinx.coroutines.CoroutineScope

@UnstableApi
@Composable
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiPaywallScreenComposable(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: BotsiPaywallUiState,
    timerManager: BotsiTimerManager,
    scope: CoroutineScope,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val contentLayout = remember(uiState) {
        (uiState as? BotsiPaywallUiState.Success)?.content?.layout?.content as? BotsiLayoutContent
    }

    val heroImageContent = (uiState as? BotsiPaywallUiState.Success)?.let {
        it.content.heroImage?.content as? BotsiHeroImageContent
    }

    val isImageHeroTransparent = remember(heroImageContent?.style) {
        heroImageContent?.style == BotsiHeroImageContentStyle.Transparent
    }

    if (isImageHeroTransparent) {
        heroImageContent?.let {
            BotsiHeroImageTransparentComposable(
                modifier = Modifier,
                content = it
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = contentLayout?.fillColor.toBrush())
        )
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            (uiState as? BotsiPaywallUiState.Success)?.content?.footer?.let {
                BotsiFooterComposable(
                    footerBlock = it,
                    scope = scope,
                    timerManager = timerManager,
                    onAction = onAction
                )
            }
        }
    ) { paddings ->
        when (uiState) {
            is BotsiPaywallUiState.None -> {}
            is BotsiPaywallUiState.Loading -> {
                Loader()
            }

            is BotsiPaywallUiState.Success -> {
                contentLayout?.let {
                    Content(
                        modifier = Modifier.padding(paddings),
                        contentLayout = it,
                        structure = uiState.content,
                        scope = scope,
                        timerManager = timerManager,
                        onAction = onAction
                    )
                }
            }

            is BotsiPaywallUiState.Error -> {
                Error(errorMessage = uiState.message)
            }
        }
    }
}

@UnstableApi
@Composable
private fun Content(
    modifier: Modifier = Modifier,
    contentLayout: BotsiLayoutContent,
    structure: BotsiPaywallContentStructure,
    scope: CoroutineScope,
    timerManager: BotsiTimerManager,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val density = LocalDensity.current
    val heroImageContent = structure.heroImage?.let {
        it.content as? BotsiHeroImageContent
    }
    val isImageHeroOverlay = remember(heroImageContent?.style) {
        heroImageContent?.style == BotsiHeroImageContentStyle.Overlay
    }
    val isImageHeroFlat = remember(heroImageContent?.style) {
        heroImageContent?.style == BotsiHeroImageContentStyle.Flat
    }
    val heroImageHeight = heroImageContent.toImageHeightPx().takeIf { isImageHeroOverlay } ?: 0f
    val heroImageContentOffset = remember(heroImageHeight) {
        heroImageHeight - (with(density) { 16.dp.toPx() })
    }
    val heroImageScrollOffsetState = remember(heroImageHeight) {
        mutableFloatStateOf(heroImageContentOffset)
    }

    val contentListScrollState = rememberLazyListState()

    val heroImageScrollConnection = remember(heroImageContent) {
        BotsiHeroImageOverlayNestedScroll(
            scrollState = contentListScrollState,
            initialOffset = heroImageContentOffset,
            offsetStateFlow = heroImageScrollOffsetState,
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .run {
                if (isImageHeroOverlay) {
                    nestedScroll(heroImageScrollConnection)
                } else {
                    this
                }
            }
    ) {
        if (isImageHeroOverlay) {
            heroImageContent?.let {
                BotsiHeroImageOverlayComposable(
                    modifier = Modifier,
                    content = it
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .run {
                    if (isImageHeroOverlay && !heroImageContent?.backgroundImage.isNullOrEmpty()) {
                        offset {
                            IntOffset(
                                y = heroImageScrollOffsetState.floatValue.toInt(),
                                x = 0
                            )
                        }
                            .background(
                                brush = contentLayout.fillColor.toBrush(),
                                shape = heroImageContent.toShape(heroImageScrollOffsetState.floatValue)
                            )
                    } else {
                        this
                    }
                },
            state = contentListScrollState,
            contentPadding = contentLayout.contentLayout.toPaddings(),
            verticalArrangement = contentLayout.contentLayout.toArrangementVertical()
        ) {
            if (isImageHeroFlat) {
                item {
                    heroImageContent?.let {
                        BotsiHeroImageFlatComposable(
                            modifier = Modifier.animateItem(),
                            content = it
                        )
                    }
                }
            }
            BotsiScopedContent(
                children = structure.content.orEmpty(),
                scope = scope,
                timerManager = timerManager,
                onAction = onAction
            )
        }

        contentLayout.topButtons
            ?.let {
                it.forEach { button ->
                    BotsiTopButtonComposable(
                        topButton = button,
                        topButtonClick = { topButton ->
                            onAction(BotsiPaywallUiAction.TopButtonClick(topButton))
                        }
                    )
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
