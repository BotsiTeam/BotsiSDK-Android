package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiHeroImageContent
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toImageHeightDp
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@Composable
internal fun BotsiHeroImageOverlayComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val image = remember(content) { content.backgroundImage }
    AsyncImage(
        modifier = modifier.height(content.toImageHeightDp()),
        model = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
internal fun BotsiHeroImageTransparentComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val image = remember(content) { content.backgroundImage }
    val color = remember(content.tint) {
        content.tint?.fillColor.toColor()
    }
    val alpha = remember(content.tint) {
        (content.tint?.opacity ?: 100f) / 100f
    }
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                drawRect(color = color, alpha = alpha)
            },
        model = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    )
}

@Composable
internal fun BotsiHeroImageFlatComposable(
    modifier: Modifier = Modifier,
    content: BotsiHeroImageContent,
) {
    val image = remember(content) { content.backgroundImage }
    val shape = remember(content) { content.toShape() }
    val paddings = remember(content) { content.layout.toPaddings() }
    val verticalOffset = remember(content) { (content.layout?.verticalOffset ?: 0).dp }

    AsyncImage(
        modifier = modifier
            .padding(paddings)
            .fillMaxWidth()
            .height(content.toImageHeightDp())
            .clip(shape)
            .offset(y = verticalOffset),
        model = image,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}