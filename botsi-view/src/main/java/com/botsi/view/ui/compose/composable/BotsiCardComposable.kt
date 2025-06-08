package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiCardContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.utils.toAlignment
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@Composable
internal fun BotsiCardComposable(
    modifier: Modifier = Modifier,
    cardBlock: BotsiPaywallBlock,
) {
    val content = cardBlock.content as? BotsiCardContent
    if (content != null) {
        val contentComposable = @Composable {
            Column(
                modifier = modifier
                    .padding(content.toPaddings())
                    .fillMaxWidth()
                    .clip(content.style.toShape())
                    .then(content.style.toBackground())
                    .then(content.style.toBorder())
                    .padding(content.contentLayout.toPaddings())
                    .offset(y = (content.verticalOffset ?: 0).dp),
                horizontalAlignment = content.contentLayout.toAlignment(),
            ) {
                cardBlock.children.orEmpty().forEach { child ->
                    BotsiContentComposable(item = child)
                }
            }
        }
        if (content.backgroundImage != null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize(),
                    model = content.backgroundImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                contentComposable()
            }
        } else {
            contentComposable()
        }
    }
}