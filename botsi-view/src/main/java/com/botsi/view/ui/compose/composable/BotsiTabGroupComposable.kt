package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiTabGroupContent
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiTabGroupComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
) {
    val content: BotsiTabGroupContent = remember { item.content as BotsiTabGroupContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }

    val contentAlignment = remember(content) { content.contentLayout.toAlignmentHorizontal() }
    val arrangement = remember(content) { content.contentLayout.toArrangementVertical() }

    Column(
        modifier = modifier
            .padding(outerPaddings)
            .fillMaxWidth()
            .offset(y = verticalOffset)
            .padding(innerPaddings),
        verticalArrangement = arrangement,
        horizontalAlignment = contentAlignment
    ) {
        item.children?.forEach { childBlock ->
            BotsiProductsContent(
                modifier = Modifier.fillMaxWidth(),
                item = childBlock,
                parentItem = item,
                align = content.contentLayout?.align
            )
        }
    }
}