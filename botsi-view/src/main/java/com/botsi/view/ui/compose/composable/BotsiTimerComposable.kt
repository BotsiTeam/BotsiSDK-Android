package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiTimerContent
import com.botsi.view.utils.toPaddings

@Composable
internal fun BotsiTimerComposable(
    modifier: Modifier = Modifier,
    content: BotsiTimerContent,
) {
    content.style?.let {
        BotsiTextComposable(
            modifier = modifier
                .padding(content.toPaddings())
                .fillMaxWidth()
                .offset(y = (content.verticalOffset ?: 0).dp),
            text = it.copy(text = "${content.beforeText} ${content.startText} ${content.afterText}")
        )
    }
}