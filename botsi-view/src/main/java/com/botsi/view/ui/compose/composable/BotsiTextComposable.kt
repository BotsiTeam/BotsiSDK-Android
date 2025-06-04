package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiText
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toTextStyle

@Composable
internal fun BotsiTextComposable(
    modifier: Modifier = Modifier,
    textContent: BotsiTextContent,
) {
    textContent.text?.let { text ->
        BotsiTextComposable(
            modifier = modifier
                .padding(textContent.toPaddings())
                .offset(y = (textContent.verticalOffset ?: 0).dp),
            text = text,
            maxLines = textContent.maxLines ?: Int.MAX_VALUE
        )
    }
}

@Composable
internal fun BotsiTextComposable(
    modifier: Modifier = Modifier,
    text: BotsiText,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        modifier = modifier,
        text = text.text.orEmpty(),
        style = text.font.toTextStyle(),
        color = text.color.toColor(text.opacity),
        fontSize = ((text.size ?: 14f) * 1.2f).sp,
        textAlign = when (text.align) {
            BotsiAlign.Left -> TextAlign.Left
            BotsiAlign.Right -> TextAlign.Right
            BotsiAlign.Center -> TextAlign.Center
            else -> TextAlign.Left
        },
        maxLines = maxLines,
    )
}