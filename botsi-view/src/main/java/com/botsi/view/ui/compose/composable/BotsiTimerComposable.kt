package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiTimerContent
import com.botsi.view.utils.toPaddings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

@Composable
internal fun BotsiTimerComposable(
    modifier: Modifier = Modifier,
    content: BotsiTimerContent,
    scope: CoroutineScope,
) {
    val paddings = remember(content) { content.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
    val startTime = remember(content) { content.startTime ?: 0 }
    var timerValue by rememberSaveable(startTime) { mutableLongStateOf(startTime) }
    var isTimerActive by rememberSaveable(startTime) { mutableStateOf(false) }
    var dateFormatted by rememberSaveable(content) { mutableStateOf(formatDate(content, timerValue)) }

    content.style?.let {
        if (!isTimerActive) {
            scope.launch {
                isTimerActive = true
                val delay = 1000L
                while (isTimerActive) {
                    delay(delay)
                    if (timerValue == 0L) {
                        isTimerActive = false
                        break
                    }
                    timerValue -= delay
                    dateFormatted = formatDate(content, timerValue)
                }
            }
        }

        BotsiTextComposable(
            modifier = modifier
                .padding(paddings)
                .fillMaxWidth()
                .offset(y = verticalOffset),
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