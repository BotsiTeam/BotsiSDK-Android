package com.botsi.view.ui.compose.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiOnOverflowBehavior
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
    val autoScale =
        remember(textContent) { textContent.onOverflow == BotsiOnOverflowBehavior.Scale }

    text?.let { text ->
        BotsiTextComposable(
            modifier = modifier
                .padding(padding)
                .fillMaxWidth()
                .offset(y = verticalOffset),
            text = text,
            maxLines = maxLines,
            autoScale = autoScale
        )
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
internal fun BotsiTextComposable(
    modifier: Modifier = Modifier,
    text: BotsiText,
    maxLines: Int = Int.MAX_VALUE,
    autoScale: Boolean = false,
) {
    val textString = remember(text) { text.text.orEmpty() }
    val textStyle = if (text.style != null) {
        text.style.font.toTextStyle()
    } else {
        text.font.toTextStyle()
    }
    val textColor = remember(text) {
        if (text.style != null) {
            text.style.color.toColor(text.opacity)
        } else {
            text.color.toColor(text.opacity)
        }
    }
    val textSize = remember(text) {
        if (text.style != null) {
            text.style.size.toFontSize()
        } else {
            text.size.toFontSize()
        }
    }
    val textAlign = remember(text) {
        when (text.align) {
            BotsiAlign.Left -> TextAlign.Left
            BotsiAlign.Right -> TextAlign.Right
            BotsiAlign.Center -> TextAlign.Center
            else -> TextAlign.Left
        }
    }

    val textComposable: @Composable (Modifier, TextUnit) -> Unit = { textModifier, textUnit ->
        Text(
            modifier = textModifier,
            text = textString,
            style = textStyle,
            color = textColor,
            fontSize = textUnit,
            textAlign = textAlign,
            maxLines = maxLines.takeIf { it > 0 } ?: Int.MAX_VALUE,
            softWrap = true
        )
    }

    if (autoScale) {
        val density = LocalDensity.current
        val textMeasurer = rememberTextMeasurer()

        BoxWithConstraints(modifier = modifier) {
            val containerWidthPx = with(density) { maxWidth.toPx() }

            val optimalFontSize by remember(textString, maxLines, textSize, containerWidthPx) {
                derivedStateOf {
                    findOptimalFontSize(
                        text = textString,
                        containerWidthPx = containerWidthPx,
                        maxLines = maxLines,
                        minFontSize = 8.sp,
                        maxFontSize = textSize,
                        stepSize = 1.sp,
                        style = textStyle,
                        textMeasurer = textMeasurer
                    )
                }
            }

            textComposable(Modifier, optimalFontSize)
        }

    } else {
        textComposable(modifier, textSize)
    }
}

private fun findOptimalFontSize(
    text: String,
    containerWidthPx: Float,
    maxLines: Int,
    minFontSize: TextUnit,
    maxFontSize: TextUnit,
    stepSize: TextUnit,
    style: TextStyle,
    textMeasurer: TextMeasurer
): TextUnit {
    var currentSize = maxFontSize

    while (currentSize >= minFontSize) {
        val testStyle = style.copy(fontSize = currentSize)

        val textLayoutResult = textMeasurer.measure(
            text = AnnotatedString(text),
            style = testStyle,
            constraints = Constraints(
                maxWidth = containerWidthPx.toInt()
            ),
            overflow = androidx.compose.ui.text.style.TextOverflow.Clip,
            softWrap = true,
            maxLines = if (maxLines == Int.MAX_VALUE) Int.MAX_VALUE else maxLines
        )

        // Check if the text fits:
        // 1. The line count should not exceed maxLines
        // 2. The text should not overflow horizontally
        // 3. If maxLines is set, check that we're not truncating text (didOverflowHeight would be true if more lines needed)
        val fitsInLines = if (maxLines == Int.MAX_VALUE) {
            // No line limit, just check width overflow
            !textLayoutResult.didOverflowWidth
        } else {
            // With line limit, check both that we don't exceed maxLines
            // and that all text fits (no height overflow means no truncation)
            textLayoutResult.lineCount <= maxLines &&
                    !textLayoutResult.didOverflowHeight &&
                    !textLayoutResult.didOverflowWidth
        }

        if (fitsInLines) {
            return currentSize
        }

        // Decrease font size by step
        currentSize = TextUnit(
            value = (currentSize.value - stepSize.value).coerceAtLeast(minFontSize.value),
            type = currentSize.type
        )

        if (currentSize <= minFontSize) {
            break
        }
    }

    return minFontSize
}