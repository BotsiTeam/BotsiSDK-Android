package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.model.content.BotsiTimerContent

internal fun LazyListScope.BotsiScopedContent(children: List<BotsiPaywallBlock>) {
    items(children) { item ->
        BotsiContentComposable(
            modifier = Modifier.animateItem(),
            item = item
        )
    }
}

@Composable
internal fun BotsiContentComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
) {
    when (item.meta?.type) {
        BotsiContentType.Text -> (item.content as? BotsiTextContent)
            ?.let { content ->
                BotsiTextComposable(
                    modifier = modifier,
                    textContent = content
                )
            }

        BotsiContentType.Button -> (item.content as? BotsiButtonContent)
            ?.let { content ->
                BotsiButtonComposable(
                    modifier = modifier,
                    buttonContent = content,
                    onClick = {},
                )
            }


        BotsiContentType.Image -> (item.content as? BotsiImageContent)?.let { content ->
            BotsiImageComposable(
                modifier = modifier,
                image = content,
            )
        }

        BotsiContentType.List -> BotsiListComposable(
            modifier = modifier,
            listBlock = item
        )

        BotsiContentType.Card -> BotsiCardComposable(
            modifier = modifier,
            cardBlock = item
        )

        BotsiContentType.Carousel -> BotsiCarouselComposable(
            modifier = modifier,
            carousel = item
        )

        BotsiContentType.Links -> (item.content as? BotsiLinksContent)?.let { content ->
            BotsiLinksComposable(
                modifier = modifier,
                content = content,
                onClick = {}
            )
        }

        BotsiContentType.Timer -> (item.content as? BotsiTimerContent)?.let { content ->
            BotsiTimerComposable(
                modifier = modifier,
                content = content,
            )
        }

        else -> {}
    }
}