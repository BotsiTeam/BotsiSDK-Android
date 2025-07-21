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
import com.botsi.view.model.ui.BotsiPaywallUiAction
import kotlinx.coroutines.CoroutineScope

internal fun LazyListScope.BotsiScopedContent(
    children: List<BotsiPaywallBlock>,
    scope: CoroutineScope,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    items(children) { item ->
        BotsiContentComposable(
            modifier = Modifier.animateItem(),
            item = item,
            scope = scope,
            onAction = onAction
        )
    }
}

@Composable
internal fun BotsiContentComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    scope: CoroutineScope,
    onAction: (BotsiPaywallUiAction) -> Unit
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
                    onClick = {
                        content.action?.let { action ->
                            onAction(BotsiPaywallUiAction.ButtonClick(action, content.actionLabel))
                        }
                    },
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
            cardBlock = item,
            scope = scope,
            onAction = onAction
        )

        BotsiContentType.Carousel -> BotsiCarouselComposable(
            modifier = modifier,
            carousel = item,
            onAction = onAction
        )

        BotsiContentType.Links -> (item.content as? BotsiLinksContent)?.let { content ->
            BotsiLinksComposable(
                modifier = modifier,
                content = content,
                onClick = { action ->
                    when (action) {
                        is com.botsi.view.model.content.BotsiButtonAction.Link -> {
                            onAction(BotsiPaywallUiAction.LinkClick(action.url))
                        }

                        is com.botsi.view.model.content.BotsiButtonAction.Custom -> {
                            onAction(BotsiPaywallUiAction.CustomAction("custom", null))
                        }

                        else -> {
                            onAction(BotsiPaywallUiAction.ButtonClick(action))
                        }
                    }
                }
            )
        }

        BotsiContentType.Timer -> (item.content as? BotsiTimerContent)?.let { content ->
            BotsiTimerComposable(
                modifier = modifier,
                content = content,
                scope = scope
            )
        }

        BotsiContentType.Products -> BotsiProductsComposable(
            modifier = modifier,
            item = item,
            onAction = onAction
        )

        else -> {}
    }
}
