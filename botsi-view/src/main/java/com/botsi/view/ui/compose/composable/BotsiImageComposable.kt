package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.utils.toContentScale
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiImageComposable(
    modifier: Modifier = Modifier,
    image: BotsiImageContent,
) {
    val paddings = remember(image) { image.toPaddings() }
    val height = remember(image) { (image.height ?: 0).dp }
    val verticalOffset = remember(image) { (image.verticalOffset ?: 0).dp }
    val imageUrl = remember(image) { image.image }
    val contentScale = remember(image) { image.toContentScale() }

    if (!imageUrl.isNullOrEmpty()) {
        AsyncImage(
            modifier = modifier
                .padding(paddings)
                .fillMaxWidth()
                .height(height)
                .offset(y = verticalOffset),
            model = imageUrl,
            contentDescription = null,
            contentScale = contentScale,
            alignment = Alignment.TopCenter
        )
    }
}