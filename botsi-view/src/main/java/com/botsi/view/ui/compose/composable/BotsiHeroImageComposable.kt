package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.utils.toImageHeightDp
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiHeroImageOverlayComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    AsyncImage(
        modifier = modifier.height(content.toImageHeightDp()),
        model = content.backgroundImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
internal fun BotsiHeroImageTransparentComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    AsyncImage(
        modifier = modifier.fillMaxSize(),
        model = content.backgroundImage,
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
    )
}

@Composable
internal fun BotsiHeroImageFlatComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    AsyncImage(
        modifier = modifier
            .padding(content.layout.toPaddings())
            .height(content.toImageHeightDp())
            .offset(y = (content.layout?.verticalOffset ?: 0).dp),
        model = content.backgroundImage,
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
    )
}