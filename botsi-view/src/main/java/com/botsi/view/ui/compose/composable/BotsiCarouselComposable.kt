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
import com.botsi.view.utils.toArrangement
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toContentPaddings
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiCarouselComposable(
    modifier: Modifier = Modifier,
    carousel: BotsiPaywallBlock,
) {
    if (!carousel.children.isNullOrEmpty()) {
        val carouselContent = remember(carousel) { carousel.content as? BotsiCarouselContent }
        val state = rememberPagerState { carousel.children.count() }

        carouselContent?.let { content ->
            val carouselContentComposable = @Composable {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    if (content.backgroundImage != null) {
                        AsyncImage(
                            modifier = modifier
                                .fillMaxSize(),
                            model = content.backgroundImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter
                        )
                    }

                    HorizontalPager(
                        modifier = modifier
                            .padding(content.toPaddings())
                            .fillMaxWidth()
                            .height((content.height ?: 0).dp)
                            .offset(y = (content.verticalOffset ?: 0).dp),
                        contentPadding = content.toContentPaddings(),
                        state = state,
                        pageSize = PageSize.Fill,
                        pageSpacing = (content.spacing ?: 0).dp
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
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(content.style.toPaddings()),
                        horizontalArrangement = content.style.toArrangement(Alignment.CenterHorizontally),
                    ) {
                        repeat(state.pageCount) { iteration ->
                            val color = if (state.currentPage == iteration) {
                                content.style?.activeColor.toColor(content.style?.activeOpacity)
                            } else {
                                content.style?.defaultColor.toColor(content.style?.defaultOpacity)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = color)
                                    .size((content.style?.size ?: 0).dp)
                            )
                        }
                    }
                }
            }
            when (content.style?.sizeOption) {
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