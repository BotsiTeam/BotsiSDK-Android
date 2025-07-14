package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toAlignmentVertical
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun BotsiProductsComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    onProductSelected: (String) -> Unit = {}
) {
    val content: BotsiProductsContent = remember { item.content as BotsiProductsContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }


    val isHorizontalLayout = remember(content) {
        content.contentLayout?.layout == BotsiLayoutDirection.Horizontal
    }

    if (isHorizontalLayout) {
        val contentAlignment = remember(content) { content.contentLayout.toAlignmentVertical() }
        val arrangement = remember(content) { content.contentLayout.toArrangementHorizontal() }
        Row(
            modifier = modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .offset(y = verticalOffset)
                .padding(innerPaddings),
            horizontalArrangement = arrangement,
            verticalAlignment = contentAlignment
        ) {
            item.children?.forEach { productBlock ->
                BotsiProductsContent(
                    modifier = Modifier.weight(1f),
                    item = productBlock,
                    parentItem = item,
                    align = content.contentLayout?.align
                )
            }
        }
    } else {
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
            item.children?.forEach { productBlock ->
                BotsiProductsContent(
                    modifier = Modifier.fillMaxWidth(),
                    item = productBlock,
                    parentItem = item,
                    align = content.contentLayout?.align
                )
            }
        }
    }
}

@Composable
internal fun BotsiProductsContent(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    parentItem: BotsiPaywallBlock,
    align: BotsiAlign? = null
) {
    when (item.meta?.type) {
        BotsiContentType.ProductItem -> BotsiProductItemComposable(
            modifier = modifier,
            item = item,
            align = align
        )

        BotsiContentType.Toggle -> BotsiProductToggleComposable(
            modifier = modifier,
            item = item,
            parentItem = parentItem,
        )

        BotsiContentType.TabControl -> BotsiTabControlComposable(
            modifier = modifier,
            item = item,
            parentItem = parentItem,
        )

        else -> {}
    }
}
