package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiTabControlContent
import com.botsi.view.model.content.BotsiTabGroupContent
import com.botsi.view.model.content.BotsiTabState
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBackgroundFillColor
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toColor
import kotlinx.coroutines.CoroutineScope

@Composable
internal fun BotsiTabControlComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    parentItem: BotsiPaywallBlock,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val content: BotsiTabControlContent = remember { item.content as BotsiTabControlContent }
    val outerPaddings = remember(content) { content.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }

    var selectedTab by remember { mutableStateOf(content.selectedTab ?: "") }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .then(content.containerStyle.toBackgroundFillColor())
                .then(content.containerStyle.toBorder())
                .offset(y = verticalOffset),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            parentItem.children?.forEach { tabGroup ->
                if (tabGroup.meta?.type == BotsiContentType.TabGroup) {
                    val tabTitle = (tabGroup.content as? BotsiTabGroupContent)?.tabTitle.orEmpty()
                    val isSelected = selectedTab == tabGroup.meta.id

                    val backgroundColor = if (isSelected) {
                        content.activeState?.stateStyle?.color?.toColor() ?: Color.Gray
                    } else {
                        content.inactiveState?.stateStyle?.color?.toColor() ?: Color.LightGray
                    }

                    val textColor = if (isSelected) {
                        content.activeState?.fontColor?.toColor() ?: Color.White
                    } else {
                        content.inactiveState?.fontColor?.toColor() ?: Color.Black
                    }

                    val paddings = if (isSelected) {
                        remember(content.activeState) { content.activeState.toPaddings() }
                    } else {
                        remember(content.inactiveState) { content.inactiveState.toPaddings() }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .padding(paddings)
                            .clickable { selectedTab = tabGroup.meta.id.orEmpty() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tabTitle,
                            style = TextStyle(
                                color = textColor,
                                fontSize = (content.tabTextSize ?: 14).sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }
                }
            }
        }

        val selectedTabContent = remember(selectedTab) { parentItem.children?.find { it.meta?.id == selectedTab } }

        selectedTabContent?.let {
            BotsiTabGroupComposable(
                item = it,
                onAction = onAction
            )
        }
    }
}
