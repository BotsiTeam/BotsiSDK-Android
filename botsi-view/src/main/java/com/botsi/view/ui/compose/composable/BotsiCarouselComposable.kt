package com.botsi.view.ui.compose.composable

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiCarouselContent
import com.botsi.view.model.content.BotsiCarouselInteractive
import com.botsi.view.model.content.BotsiCarouselLastOption
import com.botsi.view.model.content.BotsiCarouselPageControlType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toBrush
import com.botsi.view.utils.toContentPaddings
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun BotsiCarouselComposable(
    modifier: Modifier = Modifier,
    carousel: BotsiPaywallBlock,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    if (!carousel.children.isNullOrEmpty()) {
        val carouselContent = remember(carousel) { carousel.content as? BotsiCarouselContent }
        val state = rememberPagerState { carousel.children.count() }
        val scope = rememberCoroutineScope()

        carouselContent?.let { content ->
            val isSlideShowActive = remember(content) { content.slideShow == true }
            val sizeOption = remember(content) { content.style?.sizeOption }
            val interactive = remember(content) { content.timing?.interactive }
            var isSlideShowRunning by remember { mutableStateOf(true) }

            if (isSlideShowActive) {
                val slideTiming = remember(content) { content.timing?.transition ?: 0 }
                val slidePause = remember(content) { content.timing?.timing ?: 0 }
                val initialSlidePause = remember(content) { content.timing?.initialTiming ?: 0 }
                val lastOption = remember(content) { content.timing?.lastOption }
                var isInitialTimingFired by remember { mutableStateOf(false) }
                val slideAnimation = remember { tween<Float>(durationMillis = slideTiming.toInt()) }

                LaunchedEffect(initialSlidePause, slideTiming, slidePause, isSlideShowRunning) {
                    if (!isInitialTimingFired) {
                        delay(initialSlidePause)
                        isInitialTimingFired = true
                    }
                    while (isSlideShowRunning) {
                        if (state.currentPage != state.pageCount - 1) {
                            state.animateScrollToPage(
                                page = state.currentPage + 1,
                                animationSpec = slideAnimation
                            )
                        } else {
                            when (lastOption) {
                                BotsiCarouselLastOption.Stop -> {
                                    isSlideShowRunning = false
                                    break
                                }

                                BotsiCarouselLastOption.StartOver -> {
                                    state.animateScrollToPage(
                                        page = 0,
                                        animationSpec = slideAnimation
                                    )
                                }

                                else -> {}
                            }
                        }
                        delay(slidePause)
                    }
                }
            }

            val carouselContentComposable = @Composable {
                val outerPaddings = remember(content) { content.toPaddings() }
                val innerPaddings = remember(content) { content.toContentPaddings() }
                val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
                val image = remember(content) { content.backgroundImage }
                val pagerHeight = remember(content) { (content.height ?: 0).dp }
                val pageSpacing = remember(content) { (content.spacing ?: 0).dp + 4.dp }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (image != null) {
                        AsyncImage(
                            modifier = modifier
                                .fillMaxSize(),
                            model = image,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                    }

                    HorizontalPager(
                        modifier = modifier
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        if (isSlideShowActive) {
                                            when (interactive) {
                                                BotsiCarouselInteractive.Pause -> {
                                                    scope.launch {
                                                        isSlideShowRunning = false
                                                        delay(500)
                                                        isSlideShowRunning = true
                                                    }
                                                }

                                                BotsiCarouselInteractive.Stop -> {
                                                    isSlideShowRunning = false
                                                }

                                                else -> {}
                                            }
                                        }
                                    },
                                )
                            }
                            .padding(outerPaddings)
                            .fillMaxWidth()
                            .heightIn(min = pagerHeight)
                            .offset(y = verticalOffset),
                        contentPadding = innerPaddings,
                        state = state,
                        pageSize = PageSize.Fill,
                        pageSpacing = pageSpacing,
                        snapPosition = SnapPosition.Center
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            BotsiContentComposable(
                                item = carousel.children[it],
                                scope = scope,
                                onAction = onAction,
                            )
                        }
                    }
                }
            }
            val carouselPageControlComposable = @Composable {
                if (content.pageControl == true) {
                    val paddings = remember(content) { content.style.toPaddings() }
                    val arrangement =
                        remember(content) { content.style.toArrangementHorizontal(Alignment.CenterHorizontally) }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(paddings),
                        horizontalArrangement = arrangement,
                    ) {
                        repeat(state.pageCount) { iteration ->
                            val brush = remember(state.currentPage, content) {
                                if (state.currentPage == iteration) {
                                    content.style?.activeColor.toBrush(content.style?.activeOpacity)
                                } else {
                                    content.style?.defaultColor.toBrush(content.style?.defaultOpacity)
                                }
                            }
                            val size = remember { (content.style?.size ?: 0).dp }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(brush = brush)
                                    .size(size)
                            )
                        }
                    }
                }
            }
            when (sizeOption) {
                BotsiCarouselPageControlType.Overlay -> {
                    Box(contentAlignment = Alignment.BottomCenter) {
                        carouselContentComposable()
                        carouselPageControlComposable()
                    }
                }

                BotsiCarouselPageControlType.Outside -> {
                    Column {
                        carouselContentComposable()
                        Spacer(Modifier.height(8.dp))
                        carouselPageControlComposable()
                    }
                }

                else -> {}
            }
        }
    }
}