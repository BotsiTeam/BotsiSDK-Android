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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiProductToggleContent
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiProductToggleComposable(
    modifier: Modifier = Modifier,
    content: BotsiProductToggleContent,
    toggleId: String,
    onToggleChanged: (Boolean) -> Unit
) {
    // Directly access the toggleStates map
    // Since it's a SnapshotStateMap, changes to it will trigger recomposition
    var isChecked = BotsiToggleStateManager.toggleStates[toggleId] ?: content.state ?: false

    val outerPaddings = remember(content) { content.toPaddings() }
    val innerPaddings = remember(content) { content.contentLayout.toPaddings() }
    val verticalOffset = remember(content) { (content.verticaOffset ?: 0).dp }
    val toggleColor = remember(content) { content.toggleColor.toColor(content.toggleOpacity) }

    // Initial state setup - only run once when the composable is first composed
    LaunchedEffect(Unit) {
        if (!BotsiToggleStateManager.hasToggleState(toggleId)) {
            onToggleChanged(isChecked)
        }
    }

    Column(
        modifier = modifier
            .padding(outerPaddings)
            .offset(y = verticalOffset)
            .then(content.toggleStyle.toBackground())
            .then(content.toggleStyle.toBorder())
            .padding(innerPaddings),
        verticalArrangement = content.contentLayout.toArrangementVertical(
            Alignment.CenterVertically
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Show active or inactive state content based on toggle state from BotsiToggleStateManager
                val currentToggleState = BotsiToggleStateManager.toggleStates[toggleId] ?: isChecked
                val currentState = if (currentToggleState) content.activeState else content.inactiveState

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
                checked = BotsiToggleStateManager.toggleStates[toggleId] ?: isChecked,
                onCheckedChange = { newState ->
                    onToggleChanged(newState)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = toggleColor.takeIf { it != Color.Unspecified }
                        ?: SwitchDefaults.colors().checkedThumbColor,
                    uncheckedThumbColor = toggleColor.takeIf { it != Color.Unspecified }
                        ?: SwitchDefaults.colors().uncheckedThumbColor
                )
            )
        }
    }
}
