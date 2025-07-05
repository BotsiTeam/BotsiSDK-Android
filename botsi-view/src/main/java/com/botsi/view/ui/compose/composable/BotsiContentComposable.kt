package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiImageContent
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductItemContent
import com.botsi.view.model.content.BotsiProductToggleContent
import com.botsi.view.model.content.BotsiProductToggleStateContent
import com.botsi.view.model.content.BotsiProductsContent
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.model.content.BotsiTimerContent
import kotlinx.coroutines.CoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.mutableStateMapOf

internal fun LazyListScope.BotsiScopedContent(
    children: List<BotsiPaywallBlock>,
    scope: CoroutineScope
) {
    items(children) { item ->
        BotsiContentComposable(
            modifier = Modifier.animateItem(),
            item = item,
            scope = scope,
        )
    }
}

@Composable
internal fun BotsiContentComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    scope: CoroutineScope,
) {
    val toggleId = item.meta?.id ?: ""

    // Directly access the toggleStates map
    // Since it's a SnapshotStateMap, changes to it will trigger recomposition
    val toggleState = BotsiToggleStateManager.toggleStates[toggleId] ?: false

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
            cardBlock = item,
            scope = scope
        )

        BotsiContentType.Carousel -> BotsiCarouselComposable(
            modifier = modifier,
            carousel = item,
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
                scope = scope
            )
        }

        BotsiContentType.Products -> BotsiProductsComposable(
            modifier = modifier,
            item = item,
            scope = scope,
        )

        BotsiContentType.ProductItem -> BotsiProductItemComposable(
            modifier = modifier,
            item = item,
        )

        BotsiContentType.Toggle -> (item.content as? BotsiProductToggleContent)?.let { content ->
            BotsiProductToggleComposable(
                modifier = modifier,
                content = content,
                toggleId = toggleId,
                onToggleChanged = { newState ->
                    // Update the shared state
                    BotsiToggleStateManager.setToggleState(toggleId, newState)
                }
            )
        }

        BotsiContentType.ToggleOn -> {
            if (toggleState) {
                BotsiProductToggleStateComposable(
                    modifier = modifier,
                    item = item,
                    scope = scope,
                )
            }
        }

        BotsiContentType.ToggleOff -> {
            if (!toggleState) {
                BotsiProductToggleStateComposable(
                    modifier = modifier,
                    item = item,
                    scope = scope,
                )
            }
        }

        else -> {}
    }
}

/**
 * Singleton object to manage toggle states across all composables
 */
internal object BotsiToggleStateManager {
    // Use SnapshotStateMap to make the map observable
    // This is exposed as public to allow direct observation in composables
    val toggleStates: SnapshotStateMap<String, Boolean> = mutableStateMapOf()

    fun getToggleState(id: String): Boolean {
        return toggleStates[id] ?: false
    }

    fun setToggleState(id: String, state: Boolean) {
        toggleStates[id] = state
    }

    fun hasToggleState(id: String): Boolean {
        return toggleStates.containsKey(id)
    }

    // For debugging
    fun getAllToggleStates(): Map<String, Boolean> {
        return toggleStates.toMap()
    }
}
