package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.botsi.view.R
import com.botsi.view.model.content.BotsiDefaultIcon
import com.botsi.view.model.content.BotsiListContent
import com.botsi.view.model.content.BotsiListNestedContent
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.utils.toAlignment
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiListComposable(
    modifier: Modifier = Modifier,
    listBlock: BotsiPaywallBlock,
) {
    val content = listBlock.content as? BotsiListContent
    if (!listBlock.children.isNullOrEmpty()) {
        val paddings = remember(content) { content.toPaddings() }
        val verticalOffset = remember(content) { (content?.verticalOffset ?: 0).dp }
        val arrangement = remember(content) { content.toArrangementVertical() }
        Column(
            modifier = modifier
                .padding(paddings)
                .offset(y = verticalOffset),
            verticalArrangement = arrangement,
        ) {
            val childrenContent = remember(listBlock) {
                listBlock.children
                    .map { it.content }
                    .filterIsInstance<BotsiListNestedContent>()
            }

            childrenContent.forEach { child ->
                var itemHeight by remember(child.titleText) { mutableIntStateOf(0) }
                val density = LocalDensity.current
                Row(
                    modifier = Modifier.onSizeChanged { itemHeight = it.height },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val itemHeightDp = remember(itemHeight) { with(density) { itemHeight.toDp() } }
                    val connectorThickness =
                        remember(child) { (child.connectorThickness ?: 0).dp }
                    val connectorColor = remember(child) {
                        child.connectorColor.toColor(child.connectorOpacity?.toFloat())
                    }
                    val contentAlignment =
                        remember(content) { content?.iconPlacement.toAlignment() }
                    Box(
                        modifier = Modifier.height(itemHeightDp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        VerticalDivider(
                            modifier = Modifier
                                .height(itemHeightDp)
                                .clip(CircleShape),
                            thickness = connectorThickness,
                            color = connectorColor
                        )
                        Box(
                            modifier = Modifier.height(itemHeightDp),
                            contentAlignment = contentAlignment
                        ) {
                            val icon = remember(child) { child.icon }
                            val iconHeight =
                                remember(content) { content?.height?.dp ?: Dp.Unspecified }
                            val iconWidth =
                                remember(content) { content?.width?.dp ?: Dp.Unspecified }
                            if (!icon.isNullOrEmpty()) {
                                AsyncImage(
                                    modifier = Modifier.size(
                                        width = iconWidth,
                                        height = iconHeight,
                                    ),
                                    model = icon,
                                    contentDescription = null,
                                )
                            } else {
                                val defaultIcon = remember(content) { content?.defaultIcon }
                                val iconTint = remember(content) {
                                    content?.defaultColor.toColor(content?.defaultOpacity)
                                }
                                val painter = painterResource(
                                    when (defaultIcon) {
                                        BotsiDefaultIcon.Checkmark -> R.drawable.ic_checkmark_24
                                        BotsiDefaultIcon.Tick -> R.drawable.ic_tick_24
                                        BotsiDefaultIcon.Dot -> R.drawable.ic_dot_6
                                        else -> R.drawable.ic_tick_24
                                    }
                                )
                                Icon(
                                    modifier = Modifier.size(
                                        width = iconWidth,
                                        height = iconHeight,
                                    ),
                                    painter = painter,
                                    contentDescription = null,
                                    tint = iconTint
                                )
                            }
                        }
                    }

                    Column {
                        val contentSpacing = remember(content) { (content?.textSpacing ?: 0).dp }
                        val childTitleText = remember(child) { child.titleText }
                        val childCaptionText = remember(child) { child.captionText }
                        val childTitleTextStyle = remember(child) { child.titleTextStyle }
                        val childCaptionTextStyle = remember(child) { child.captionTextStyle }
                        if (!childTitleText.isNullOrEmpty()) {
                            childTitleTextStyle?.let {
                                BotsiTextComposable(
                                    text = it.copy(text = childTitleText),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(contentSpacing))
                        if (!childCaptionText.isNullOrEmpty()) {
                            childCaptionTextStyle?.let {
                                BotsiTextComposable(
                                    text = it.copy(text = child.captionText),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}