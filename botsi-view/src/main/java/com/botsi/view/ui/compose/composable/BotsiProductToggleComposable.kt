package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiContentType
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductToggleContent
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toBrush
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiProductToggleComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    parentItem: BotsiPaywallBlock,
    timerManager: BotsiTimerManager,
    selectedProductId: Long?,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val content = remember { item.content as BotsiProductToggleContent }
    var isChecked by remember { mutableStateOf(false) }

    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticaOffset ?: 0).dp }
    val toggleColor = remember(content) { content.toggleColor.toColor(content.toggleOpacity) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(outerPaddings)
                .fillMaxWidth()
                .offset(y = verticalOffset)
                .then(content.toggleStyle.toBackground())
                .then(content.toggleStyle.toBorder())
                .padding(innerPaddings),
            horizontalArrangement = content.contentLayout.toArrangementHorizontal(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val currentState = if (isChecked) content.activeState else content.inactiveState

                if (!currentState?.text.isNullOrEmpty()) {
                    currentState.textStyle?.let { textStyle ->
                        BotsiTextComposable(
                            text = textStyle.copy(text = currentState.text)
                        )
                    }
                }

                if (!currentState?.secondaryText.isNullOrEmpty()) {
                    currentState.secondaryTextStyle?.let { textStyle ->
                        BotsiTextComposable(
                            text = textStyle.copy(text = currentState.secondaryText)
                        )
                    }
                }
            }

            Switch(
                checked = isChecked,
                onCheckedChange = { newState -> isChecked = newState },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = toggleColor.takeIf { it != Color.Unspecified }
                        ?: SwitchDefaults.colors().checkedThumbColor,
                    uncheckedThumbColor = toggleColor.takeIf { it != Color.Unspecified }
                        ?: SwitchDefaults.colors().uncheckedThumbColor
                )
            )
        }

        parentItem.children?.forEach {
            when (it.meta?.type) {
                BotsiContentType.ToggleOn -> {
                    if (isChecked) {
                        BotsiProductToggleStateComposable(
                            modifier = modifier,
                            item = it,
                            timerManager = timerManager,
                            selectedProductId = selectedProductId,
                            onAction = onAction
                        )
                    }
                }

                BotsiContentType.ToggleOff -> {
                    if (!isChecked) {
                        BotsiProductToggleStateComposable(
                            modifier = modifier,
                            item = it,
                            timerManager = timerManager,
                            selectedProductId = selectedProductId,
                            onAction = onAction
                        )
                    }
                }

                else -> {}
            }
        }
    }
}
