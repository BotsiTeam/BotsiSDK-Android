package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiTextContent

internal fun LazyListScope.BotsiContentComposable(children: List<BotsiPaywallBlock>) {
    items(children.map { it.content }) { item ->
        when (item) {
            is BotsiTextContent -> BotsiTextComposable(
                modifier = Modifier.animateItem(),
                textContent = item
            )

            is BotsiButtonContent -> BotsiButtonComposable(
                modifier = Modifier.animateItem(),
                buttonContent = item,
                onClick = {},
            )

            else -> {}
        }
    }
}