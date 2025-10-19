package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.CoroutineScope
import kotlin.collections.orEmpty

@Composable
internal fun BotsiFooterComposable(
    modifier: Modifier = Modifier,
    footerBlock: BotsiPaywallBlock,
    scope: CoroutineScope,
    timerManager: BotsiTimerManager,
    selectedProductId: Long?,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val footerContent = remember(footerBlock) {
        footerBlock.content as? BotsiFooterContent
    }
    val paddings = remember(footerContent) { footerContent.toPaddings() }
    val arrangement = remember(footerContent) { footerContent.toArrangementVertical() }
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .then(footerContent?.style.toBackground())
            .then(footerContent?.style.toBorder())
            .padding(paddings),
        verticalArrangement = arrangement,
    ) {
        BotsiScopedContent(
            children = footerBlock.children.orEmpty(),
            scope = scope,
            timerManager = timerManager,
            selectedProductId = selectedProductId,
            onAction = onAction
        )
    }
}
