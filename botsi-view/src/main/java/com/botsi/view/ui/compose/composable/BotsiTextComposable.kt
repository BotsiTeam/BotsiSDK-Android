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
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.LayoutDirection
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
    val textStyle = remember(text) {
        if (text.style != null) {
            text.style.font.toTextStyle()
        } else {
            text.font.toTextStyle()
        }
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

    val textComposable: @Composable (Modifier, TextUnit) -> Unit = { modifier, textUnit ->
        Text(
            modifier = modifier,
            text = textString,
            style = textStyle,
            color = textColor,
            fontSize = textUnit,
            textAlign = textAlign,
            maxLines = maxLines.takeIf { it > 0 } ?: Int.MAX_VALUE,
            softWrap = maxLines >= 0
        )
    }

    if (autoScale) {
        val density = LocalDensity.current
        val fontFamilyResolver = LocalFontFamilyResolver.current

        BoxWithConstraints(modifier = Modifier) {
            val containerWidthPx = with(density) { maxWidth.toPx() }

            val optimalFontSize by remember(text.text, maxLines, textSize) {
                derivedStateOf {
                    findOptimalFontSize(
                        text = text.text.orEmpty(),
                        containerWidthPx = containerWidthPx,
                        maxLines = maxLines,
                        minFontSize = 8.sp,
                        maxFontSize = textSize,
                        stepSize = 1.sp,
                        style = textStyle,
                        density = density,
                        fontFamilyResolver = fontFamilyResolver
                    )
                }
            }

            textComposable(modifier, optimalFontSize)
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
    density: androidx.compose.ui.unit.Density,
    fontFamilyResolver: FontFamily.Resolver
): TextUnit {
    var currentSize = maxFontSize

    while (currentSize >= minFontSize) {
        val testStyle = style.copy(fontSize = currentSize)

        val textLayoutResult = TextMeasurer(
            defaultFontFamilyResolver = fontFamilyResolver,
            defaultDensity = density,
            defaultLayoutDirection = LayoutDirection.Ltr,
        ).measure(
            text = AnnotatedString(text),
            style = testStyle,
            constraints = Constraints(
                maxWidth = containerWidthPx.toInt()
            )
        )

        // Check if the text fits within the specified max lines
        if (textLayoutResult.lineCount <= maxLines) {
            return currentSize
        }

        // Decrease font size by step
        val newSize = with(density) {
            (currentSize.toPx() - stepSize.toPx()).toSp()
        }
        currentSize = if (newSize > minFontSize) newSize else minFontSize
    }

    return minFontSize
}