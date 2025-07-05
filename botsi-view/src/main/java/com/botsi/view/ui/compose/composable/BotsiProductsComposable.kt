package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    scope: CoroutineScope,
    onProductSelected: (String) -> Unit = {}
) {
    val content: BotsiProductsContent = remember { item.content as BotsiProductsContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }


    val isHorizontalLayout = remember(content) {
        content.contentLayout?.layout?.lowercase() == "horizontal"
    }

    if (isHorizontalLayout) {
        val contentAlignment = remember(content) { content.contentLayout.toAlignmentVertical() }
        val arrangement = remember(content) { content.contentLayout.toArrangementHorizontal() }
        Row(
            modifier = Modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .offset(y = verticalOffset)
                .padding(innerPaddings),
            horizontalArrangement = arrangement,
            verticalAlignment = contentAlignment
        ) {
            item.children?.forEach { productBlock ->
                BotsiContentComposable(
                    item = productBlock,
                    scope = scope,
                )
            }
        }
    } else {
        val contentAlignment = remember(content) { content.contentLayout.toAlignmentHorizontal() }
        val arrangement = remember(content) { content.contentLayout.toArrangementVertical() }
        Column(
            modifier = Modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .offset(y = verticalOffset)
                .padding(innerPaddings),
            verticalArrangement = arrangement,
            horizontalAlignment = contentAlignment
        ) {
            item.children?.forEach { productBlock ->
                BotsiContentComposable(
                    item = productBlock,
                    scope = scope,
                )
            }
        }
    }
}