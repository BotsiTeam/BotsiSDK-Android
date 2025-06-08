package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.botsi.view.model.content.BotsiFooterContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.utils.toArrangement
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings
import kotlin.collections.orEmpty

@Composable
internal fun BotsiFooterComposable(
    modifier: Modifier = Modifier,
    footerBlock: BotsiPaywallBlock,
) {
    val footerContent = remember(footerBlock) {
        footerBlock.content as? BotsiFooterContent
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .then(footerContent?.style.toBackground())
            .then(footerContent?.style.toBorder())
            .padding(footerContent.toPaddings()),
        verticalArrangement = footerContent.toArrangement(),
    ) {
        BotsiScopedContent(
            children = footerBlock.children.orEmpty()
        )
    }
}