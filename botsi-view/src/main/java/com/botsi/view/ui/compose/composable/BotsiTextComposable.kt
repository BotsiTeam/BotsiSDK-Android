package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiText
import com.botsi.view.model.content.BotsiTextContent
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toFontSize
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toTextStyle

@Composable
internal fun BotsiTextComposable(
    modifier: Modifier = Modifier,
    textContent: BotsiTextContent,
) {
    val text = remember(textContent) { textContent.text }
    val padding = remember(textContent) { textContent.toPaddings() }
    val verticalOffset = remember(textContent) { (textContent.verticalOffset ?: 0).dp }
    val maxLines = remember(textContent) { textContent.maxLines ?: Int.MAX_VALUE }

    text?.let { text ->
        BotsiTextComposable(
            modifier = modifier
                .padding(padding)
                .offset(y = verticalOffset),
            text = text,
            maxLines = maxLines
        )
    }
}

@Composable
internal fun BotsiTextComposable(
    modifier: Modifier = Modifier,
    text: BotsiText,
    maxLines: Int = Int.MAX_VALUE
) {
    val textString = remember(text) { text.text.orEmpty() }
    val textStyle = remember(text) { text.font.toTextStyle() }
    val textColor = remember(text) { text.color.toColor(text.opacity) }
    val textSize = remember(text) { text.size.toFontSize() }
    val textAlign = remember(text) {
        when (text.align) {
            BotsiAlign.Left -> TextAlign.Left
            BotsiAlign.Right -> TextAlign.Right
            BotsiAlign.Center -> TextAlign.Center
            else -> TextAlign.Left
        }
    }

    Text(
        modifier = modifier,
        text = textString,
        style = textStyle,
        color = textColor,
        fontSize = textSize,
        textAlign = textAlign,
        maxLines = maxLines,
        softWrap = maxLines > 1
    )
}