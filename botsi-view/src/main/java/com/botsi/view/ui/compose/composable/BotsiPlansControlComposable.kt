package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiPlansControlContent
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiPlansControlComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    parentItem: BotsiPaywallBlock,
    timerManager: BotsiTimerManager,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val content: BotsiPlansControlContent = remember { item.content as BotsiPlansControlContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
    val contentAlignment = remember(content) { content.contentLayout?.align.toAlignmentHorizontal() }

    var isExpanded by remember { mutableStateOf(content.state ?: false) }

    val currentText = if (isExpanded) {
        content.morePlansShownText
    } else {
        content.defaultText
    }

    val mainPlans = remember(parentItem) {
        parentItem.children?.find { it.meta?.type == BotsiContentType.MainPlans }
    }

    val morePlans = remember(parentItem) {
        parentItem.children?.find { it.meta?.type == BotsiContentType.MorePlans }
    }

    val morePlansBottomSheet = remember(parentItem) {
        parentItem.children?.find { it.meta?.type == BotsiContentType.MorePlansSheet }
    }

    if (mainPlans != null) {
        BotsiPlansComposable(
            item = mainPlans,
            timerManager = timerManager,
            onAction = onAction
        )
    }

    Box(
        modifier = modifier
            .padding(outerPaddings)
            .fillMaxWidth()
            .offset(y = verticalOffset)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(content.style.toBorder())
                .then(content.style.toBackground())
                .clickable { isExpanded = !isExpanded }
                .padding(innerPaddings),
            horizontalAlignment = contentAlignment
        ) {
            currentText?.textStyle?.let {
                if (!currentText.text.isNullOrEmpty()) {
                    BotsiTextComposable(
                        text = it.copy(text = currentText.text),
                    )
                }
            }
            currentText?.secondaryTextStyle?.let {
                if (!currentText.secondaryText.isNullOrEmpty()) {
                    BotsiTextComposable(
                        text = it.copy(text = currentText.secondaryText),
                    )
                }
            }
        }
    }

    if (morePlans != null && isExpanded) {
        BotsiPlansComposable(
            item = morePlans,
            timerManager = timerManager,
            onAction = onAction
        )
    }

    if (morePlansBottomSheet != null && isExpanded) {
        BotsiMorePlansSheetComposable(
            item = morePlansBottomSheet,
            timerManager = timerManager,
            onAction = onAction,
            onCloseClick = { isExpanded = !isExpanded }
        )
    }
}
