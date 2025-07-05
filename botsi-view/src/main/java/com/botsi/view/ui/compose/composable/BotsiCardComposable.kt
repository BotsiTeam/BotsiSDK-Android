package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.model.content.BotsiCardContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun BotsiCardComposable(
    modifier: Modifier = Modifier,
    cardBlock: BotsiPaywallBlock,
    scope: CoroutineScope
) {
    val content = remember(cardBlock) { cardBlock.content as? BotsiCardContent }
    val shape = remember(content) { content?.style.toShape() }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content?.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content?.verticalOffset ?: 0).dp }
    val alignment = remember(content) { content?.contentLayout.toAlignmentHorizontal() }
    val image = remember(content) { content?.backgroundImage }

    if (content != null) {
        val contentComposable = @Composable {
            Column(
                modifier = modifier
                    .padding(outerPaddings)
                    .fillMaxWidth()
                    .clip(shape)
                    .then(content.style.toBackground())
                    .then(content.style.toBorder())
                    .padding(innerPaddings)
                    .offset(y = verticalOffset),
                horizontalAlignment = alignment,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cardBlock.children.orEmpty().forEach { child ->
                    BotsiContentComposable(
                        item = child,
                        scope = scope,
                    )
                }
            }
        }
        if (image != null) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = modifier
                        .fillMaxSize(),
                    model = image,
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