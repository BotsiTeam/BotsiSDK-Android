package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
    if (!image.image.isNullOrEmpty()) {
        AsyncImage(
            modifier = modifier
                .padding(image.toPaddings())
                .fillMaxWidth()
                .height((image.height ?: 0).dp)
                .offset(y = (image.verticalOffset ?: 0).dp),
            model = image.image,
            contentDescription = null,
            contentScale = image.toContentScale(),
            alignment = Alignment.TopCenter
        )
    }
}