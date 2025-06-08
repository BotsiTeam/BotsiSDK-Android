package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.botsi.view.utils.toArrangement
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiListComposable(
    modifier: Modifier = Modifier,
    listBlock: BotsiPaywallBlock,
) {
    val content = listBlock.content as? BotsiListContent
    if (!listBlock.children.isNullOrEmpty()) {
        Column(
            modifier = modifier
                .padding(content.toPaddings())
                .offset(y = (content?.verticalOffset ?: 0).dp),
            verticalArrangement = content.toArrangement(),
        ) {
            val childrenContent = listBlock.children
                .map { it.content }
                .filterIsInstance<BotsiListNestedContent>()

            childrenContent.forEach { child ->
                var itemHeight by remember(child.titleText) { mutableIntStateOf(0) }
                val density = LocalDensity.current
                Row(
                    modifier = Modifier.onSizeChanged { itemHeight = it.height },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier.height(with(density) { itemHeight.toDp() }),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        VerticalDivider(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 4.dp)
                                .height(with(density) { itemHeight.toDp() })
                                .clip(CircleShape),
                            thickness = (content?.connectorThickness ?: 0).dp,
                            color = content?.connectorColor.toColor(content?.connectorOpacity)
                        )
                        Box(
                            modifier = Modifier.height(with(density) { itemHeight.toDp() }),
                            contentAlignment = content?.iconPlacement.toAlignment()
                        ) {
                            if (!child.icon.isNullOrEmpty()) {
                                AsyncImage(
                                    modifier = Modifier.size(
                                        width = content?.width?.dp ?: Dp.Unspecified,
                                        height = content?.height?.dp ?: Dp.Unspecified,
                                    ),
                                    model = child.icon,
                                    contentDescription = null,
                                )
                            } else {
                                val painter = painterResource(
                                    when (content?.defaultIcon) {
                                        BotsiDefaultIcon.Tick -> R.drawable.ic_tick_24
                                        BotsiDefaultIcon.Checkmark -> R.drawable.ic_checkmark_24
                                        BotsiDefaultIcon.Dot -> R.drawable.ic_dot_6
                                        else -> R.drawable.ic_tick_24
                                    }
                                )
                                Icon(
                                    modifier = Modifier.size(
                                        width = content?.width?.dp ?: Dp.Unspecified,
                                        height = content?.height?.dp ?: Dp.Unspecified,
                                    ),
                                    painter = painter,
                                    contentDescription = null,
                                    tint = content?.defaultColor.toColor(content?.defaultOpacity)
                                )
                            }
                        }
                    }

                    Column {
                        if (!child.titleText.isNullOrEmpty()) {
                            child.titleTextStyle?.let {
                                BotsiTextComposable(
                                    text = it.copy(text = child.titleText),
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height((content?.textSpacing ?: 0).dp))
                        if (!child.captionText.isNullOrEmpty()) {
                            child.captionTextStyle?.let {
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