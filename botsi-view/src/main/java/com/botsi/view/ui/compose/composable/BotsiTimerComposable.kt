package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiTimerContent
import com.botsi.view.model.content.BotsiTimerMode
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.CoroutineScope
import java.util.Date

@Composable
internal fun BotsiTimerComposable(
    modifier: Modifier = Modifier,
    content: BotsiTimerContent,
    scope: CoroutineScope,
    timerManager: BotsiTimerManager,
    timerInternalId: String?,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val paddings = remember(content) { content.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
    val startTime = remember(content) {
        if (content.timerMode == BotsiTimerMode.DeveloperDefined) {
            timerManager.timerEndAtDate(content.timerId.orEmpty()).time - System.currentTimeMillis()
        } else {
            content.startTime ?: 0
        }
    }
    var timerValue by rememberSaveable(startTime) { mutableLongStateOf(startTime) }
    var dateFormatted by rememberSaveable(timerValue) {
        mutableStateOf(
            formatDate(
                content,
                timerValue
            )
        )
    }

    LaunchedEffect(timerInternalId, startTime) {
        // Start timer using timer manager
        timerInternalId?.let { internalId ->
            timerManager.startTimer(
                timerInternalId = internalId,
                timerId = content.timerId,
                timerMode = content.timerMode,
                startTime = timerValue,
                scope = scope,
                onTimerUpdate = { value ->
                    timerValue = value
                    dateFormatted = formatDate(content, value)
                },
                onTimerFinished = {
                    // Handle timer finished
                    if (content.triggerCustomAction == true && content.customActionId != null) {
                        onAction(
                            BotsiPaywallUiAction.TimerEnd(
                                content.customActionId,
                            )
                        )
                    }
                }
            )
        }
    }

    content.style?.let {
        BotsiTextComposable(
            modifier = modifier
                .padding(paddings)
                .fillMaxWidth()
                .offset(y = verticalOffset),
            maxLines = 1,
            autoScale = true,
            text = it.copy(text = "${content.beforeText} $dateFormatted ${content.afterText}")
        )
    }
}

private fun formatDate(content: BotsiTimerContent, timerValue: Long): String {
    return runCatching {
        val formattedDate = content.dateFormat?.format(Date(timerValue)).orEmpty()
        if (content.noJavaFormatSupportSeparatorValues != null) {
            formattedDate
                .split(" ")
                .mapIndexed { index, value -> "$value${content.noJavaFormatSupportSeparatorValues[index]}" }
                .joinToString(" ")
        } else {
            formattedDate
        }
    }.getOrElse { Date(timerValue).toString() }
}
