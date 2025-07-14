package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductToggleStateContent
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toAlignmentVertical
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun BotsiProductToggleStateComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
) {
    val content = remember(item) { item.content as? BotsiProductToggleStateContent }
    if (content == null) return

    if (item.children.isNullOrEmpty()) return

    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }

    val layoutComponent: @Composable () -> Unit = {
        item.children.forEach { child ->
            BotsiProductsContent(
                item = child,
                parentItem = item,
                align = content.contentLayout?.align
            )
        }
    }

    when (content.contentLayout?.layout) {
        BotsiLayoutDirection.Horizontal -> {
            val contentAlignment = remember(content) { content.contentLayout.toAlignmentVertical() }
            val arrangement = remember(content) { content.contentLayout.toArrangementHorizontal() }
            Row(
                modifier = modifier
                    .padding(outerPaddings)
                    .offset(y = verticalOffset)
                    .padding(innerPaddings),
                horizontalArrangement = arrangement,
                verticalAlignment = contentAlignment
            ) {
                layoutComponent()
            }
        }

        else -> {
            val contentAlignment = remember(content) { content.contentLayout.toAlignmentHorizontal() }
            val arrangement = remember(content) { content.contentLayout.toArrangementVertical() }
            Column(
                modifier = modifier
                    .padding(outerPaddings)
                    .offset(y = verticalOffset)
                    .padding(innerPaddings),
                verticalArrangement = arrangement,
                horizontalAlignment = contentAlignment
            ) {
                layoutComponent()
            }
        }
    }
}