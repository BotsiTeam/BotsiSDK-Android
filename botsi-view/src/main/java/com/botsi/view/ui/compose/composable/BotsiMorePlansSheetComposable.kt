package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.botsi.view.R
import com.botsi.view.model.content.BotsiMorePlansSheetContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BotsiMorePlansSheetComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    onCloseClick: () -> Unit,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val content: BotsiMorePlansSheetContent = remember { item.content as BotsiMorePlansSheetContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
    val contentAlignment = remember(content) { content.contentLayout?.align.toAlignmentHorizontal() }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onCloseClick,
        containerColor = content.plansStyles?.color.toColor(content.plansStyles?.opacity),
        shape = content.plansStyles.toShape(),
        dragHandle = {},
    ) {
        Column(
            modifier = Modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .padding(innerPaddings)
                .offset(y = verticalOffset),
            horizontalAlignment = contentAlignment
        ) {
            // Close button
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    // Title text
                    content.titleTextStyle?.let { titleStyle ->
                        if (!content.titleText.isNullOrEmpty()) {
                            BotsiTextComposable(
                                modifier = Modifier.fillMaxWidth(),
                                text = titleStyle.copy(text = content.titleText),
                            )
                        }
                    }

                    // Secondary text
                    content.secondaryTextStyle?.let { secondaryStyle ->
                        if (!content.secondaryText.isNullOrEmpty()) {
                            BotsiTextComposable(
                                modifier = Modifier.fillMaxWidth(),
                                text = secondaryStyle.copy(text = content.secondaryText),
                            )
                        }
                    }
                }
                content.closeButtonStyles?.let { closeButtonStyle ->
                    Icon(
                        modifier = Modifier
                            .then(closeButtonStyle.toBorder())
                            .then(closeButtonStyle.toBackground())
                            .clickable(onClick = onCloseClick)
                            .size((content.iconSize?.toIntOrNull() ?: 16).dp),
                        painter = painterResource(R.drawable.ic_close_24),
                        contentDescription = "Close",
                        tint = content.iconColor.toColor(),
                    )
                }
            }

            // Render child plans
            item.children?.forEach { childBlock ->
                BotsiProductsContent(
                    modifier = Modifier.fillMaxWidth(),
                    item = childBlock,
                    parentItem = item,
                    align = content.contentLayout?.align,
                    onAction = onAction
                )
            }

            // Render child plans
            item.children?.forEach { childBlock ->
                BotsiContentComposable(
                    modifier = Modifier.fillMaxWidth(),
                    item = childBlock,
                    scope = rememberCoroutineScope(),
                    onAction = onAction
                )
            }
        }
    }
}
