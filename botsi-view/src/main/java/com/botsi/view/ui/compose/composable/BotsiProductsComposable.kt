package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toAlignmentVertical
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiProductsComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    timerManager: BotsiTimerManager,
    selectedProductId: Long?,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val content: BotsiProductsContent = remember { item.content as BotsiProductsContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }

    LaunchedEffect(item) {
        val products = item.children?.filter { it.meta?.type == BotsiContentType.ProductItem }
        if (products?.size == 1) {
            onAction(
                BotsiPaywallUiAction.ProductSelected(
                    productId = products.first().meta?.productId
                )
            )
        }
    }

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
                .height(IntrinsicSize.Min)
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
                    align = content.contentLayout?.align,
                    timerManager = timerManager,
                    isHorizontal = true,
                    selectedProductId = selectedProductId,
                    onAction = onAction,
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
                    align = content.contentLayout?.align,
                    timerManager = timerManager,
                    selectedProductId = selectedProductId,
                    onAction = onAction
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
    align: BotsiAlign? = null,
    timerManager: BotsiTimerManager,
    isHorizontal: Boolean = false,
    selectedProductId: Long?,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    when (item.meta?.type) {
        BotsiContentType.ProductItem -> BotsiProductItemComposable(
            modifier = modifier,
            item = item,
            align = align,
            isHorizontal = isHorizontal,
            selectedProductId = selectedProductId,
            onAction = onAction
        )

        BotsiContentType.Toggle -> BotsiProductToggleComposable(
            modifier = modifier,
            item = item,
            parentItem = parentItem,
            timerManager = timerManager,
            selectedProductId = selectedProductId,
            onAction = onAction
        )

        BotsiContentType.TabControl -> BotsiTabControlComposable(
            modifier = modifier,
            item = item,
            parentItem = parentItem,
            timerManager = timerManager,
            selectedProductId = selectedProductId,
            onAction = onAction
        )

        BotsiContentType.PlansControl -> BotsiPlansControlComposable(
            modifier = modifier,
            item = item,
            parentItem = parentItem,
            timerManager = timerManager,
            selectedProductId = selectedProductId,
            onAction = onAction
        )

        else -> {}
    }
}
