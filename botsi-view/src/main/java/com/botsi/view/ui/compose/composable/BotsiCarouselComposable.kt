package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiCarouselContent
import com.botsi.view.model.content.BotsiCarouselPageControlType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.utils.toAlignment
import com.botsi.view.utils.toArrangement
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toContentPaddings
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@Composable
internal fun BotsiCarouselComposable(
    modifier: Modifier = Modifier,
    carousel: BotsiPaywallBlock,
) {
    if (!carousel.children.isNullOrEmpty()) {
        val carouselContent = remember(carousel) { carousel.content as? BotsiCarouselContent }
        val state = rememberPagerState { carousel.children.count() }

        carouselContent?.let { content ->
            val sizeOption = remember(content) { content.style?.sizeOption }

            val carouselContentComposable = @Composable {
                val outerPaddings = remember(content) { content.toPaddings() }
                val innerPaddings = remember(content) { content.toContentPaddings() }
                val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
                val image = remember(content) { content.backgroundImage }
                val pagerHeight = remember(content) { (content.height ?: 0).dp }
                val pageSpacing = remember(content) { (content.spacing ?: 0).dp }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (content.backgroundImage != null) {
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
                            .padding(outerPaddings)
                            .fillMaxWidth()
                            .height(pagerHeight)
                            .offset(y = verticalOffset),
                        contentPadding = innerPaddings,
                        state = state,
                        pageSize = PageSize.Fill,
                        pageSpacing = pageSpacing
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            BotsiContentComposable(item = carousel.children[it])
                        }
                    }
                }
            }
            val carouselPageControlComposable = @Composable {
                if (content.pageControl == true) {
                    val paddings = remember(content) { content.style.toPaddings() }
                    val arrangement = remember(content) { content.style.toArrangement(Alignment.CenterHorizontally) }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(paddings),
                        horizontalArrangement = arrangement,
                    ) {
                        repeat(state.pageCount) { iteration ->
                            val color = remember(state.currentPage, content) {
                                if (state.currentPage == iteration) {
                                    content.style?.activeColor.toColor(content.style?.activeOpacity)
                                } else {
                                    content.style?.defaultColor.toColor(content.style?.defaultOpacity)
                                }
                            }
                            val size = remember { (content.style?.size ?: 0).dp }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = color)
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
                        carouselPageControlComposable()
                    }
                }

                else -> {}
            }
        }
    }
}