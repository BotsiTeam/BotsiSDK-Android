package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.content.BotsiLayoutDirection
import com.botsi.view.model.content.BotsiLinksContent
import com.botsi.view.model.content.BotsiLinksStyle
import com.botsi.view.model.content.BotsiLinksText
import com.botsi.view.utils.toArrangementVertical
import com.botsi.view.utils.toArrangementHorizontal
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toFontSize
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toTextStyle
import kotlin.math.roundToInt

@Composable
internal fun BotsiLinksComposable(
    modifier: Modifier = Modifier,
    content: BotsiLinksContent,
    onClick: (BotsiButtonAction) -> Unit
) {
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var calculatedTextSize by remember { mutableStateOf<TextUnit?>(null) }

    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val items = remember(content) {
        val items = mutableListOf<Pair<BotsiLinksText?, BotsiButtonAction>>()
        if (content.hasTermOfService == true) items.add(
            content.termOfService to BotsiButtonAction.Link(
                content.termOfService?.url.orEmpty()
            )
        )
        if (content.hasPrivacyPolicy == true) items.add(
            content.privacyPolicy to BotsiButtonAction.Link(
                content.privacyPolicy?.url.orEmpty()
            )
        )
        if (content.hasRestoreButton == true) items.add(content.restoreButton to BotsiButtonAction.Restore)
        if (content.hasLoginButton == true) items.add(content.loginButton to BotsiButtonAction.Login)
        items.filter { it.first != null }
    }
    val containerArrangement = remember(content) { content.contentLayout.toArrangementVertical() }
    val containerPaddings = remember(content) { content.toPaddings() }
    val verticalOffset = remember(content) { (content.verticalOffset ?: 0).dp }
    val contentAlignment = remember(content) { content.contentLayout?.layout }

    val hostModifier = modifier
        .fillMaxWidth()
        .padding(containerPaddings)
        .onSizeChanged { containerSize = it }
        .offset(y = verticalOffset)

    val isDividersVisible = remember(content) {
        (content.style?.dividersThickness ?: 0) > 0 &&
                contentAlignment == BotsiLayoutDirection.Horizontal
    }
    LaunchedEffect(containerSize, items, content.style?.size) {
        if (containerSize != IntSize.Zero && items.isNotEmpty()) {
            val spacerCount = items.size - 1
            var availableWidth = containerSize.width - with(density) {
                (containerArrangement.spacing * spacerCount).toPx().toInt()
            }
            var availableHeight = containerSize.height - with(density) {
                (containerArrangement.spacing * spacerCount).toPx().toInt()
            }

            if (isDividersVisible) {
                val fullDividerWidth = (content.style?.dividersThickness ?: 0).dp * spacerCount
                val dividersSpace = fullDividerWidth + containerArrangement.spacing * spacerCount

                val dividersSpacedPx = with(density) { dividersSpace.toPx().roundToInt() }
                availableWidth -= dividersSpacedPx
                availableHeight -= dividersSpacedPx
            }

            // Get the incoming font size from style, prioritize it
            val incomingFontSize = content.style?.size.toFontSize()
            var testSize = incomingFontSize
            var finalSize = 10.sp

            // Helper function to check if text fits within bounds
            fun checkTextFits(fontSize: TextUnit): Boolean {
                var estimatedTotalWidth = 0
                var estimatedTotalHeight = 0
                for ((_, text) in items.withIndex()) {
                    val textLayoutResult = textMeasurer.measure(
                        text = text.first?.text.orEmpty(),
                        style = TextStyle(fontSize = fontSize)
                    )

                    estimatedTotalWidth += textLayoutResult.size.width
                    estimatedTotalHeight += textLayoutResult.size.height
                }

                return when (content.contentLayout?.layout) {
                    BotsiLayoutDirection.Vertical -> estimatedTotalHeight <= availableHeight
                    else -> estimatedTotalWidth <= availableWidth
                }
            }

            // First try the incoming font size
            if (checkTextFits(incomingFontSize)) {
                finalSize = incomingFontSize
            } else {
                // If incoming size doesn't fit, decrease it until it fits
                val incomingSizeValue = incomingFontSize.value
                val minSize = 10f

                for (sizeValue in incomingSizeValue.toInt() downTo minSize.toInt()) {
                    testSize = sizeValue.sp
                    if (checkTextFits(testSize)) {
                        finalSize = testSize
                        break
                    }
                }
            }

            calculatedTextSize = finalSize
        }
    }

    val contentComposable: @Composable () -> Unit = {
        val textHeight = remember(calculatedTextSize) {
            calculatedTextSize?.let {
                textMeasurer.measure(
                    text = items.first().first?.text.orEmpty(),
                    style = TextStyle(fontSize = it)
                ).size.height
            } ?: 0
        }
        val textHeightDp = remember(textHeight) { with(density) { textHeight.toDp() } }

        items.forEachIndexed { index, item ->
            item.first?.let {
                BotsiLinksTextComposable(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                onClick(item.second)
                            },
                        ),
                    content = it,
                    style = content.style,
                    staticFontSize = calculatedTextSize
                )


                if (index != items.lastIndex &&
                    isDividersVisible &&
                    !content.style?.dividersColor.isNullOrEmpty()
                ) {
                    val dividerThickness =
                        remember(content) { (content.style.dividersThickness ?: 0).dp }
                    val dividerColor = remember(content) {
                        content.style.dividersColor.toColor(content.style.dividersOpacity)
                    }
                    when (contentAlignment) {
                        BotsiLayoutDirection.Horizontal -> {
                            VerticalDivider(
                                modifier = Modifier
                                    .height(textHeightDp),
                                thickness = dividerThickness,
                                color = dividerColor
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    if (content.contentLayout?.layout == BotsiLayoutDirection.Vertical) {
        val arrangement = remember(content) {
            content.contentLayout.toArrangementVertical(
                Alignment.CenterVertically
            )
        }
        Column(
            modifier = hostModifier,
            verticalArrangement = arrangement,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            contentComposable()
        }
    } else {
        val arrangement = remember(content) {
            content.contentLayout.toArrangementHorizontal(
                Alignment.CenterHorizontally
            )
        }
        Row(
            modifier = hostModifier,
            horizontalArrangement = arrangement,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            contentComposable()
        }
    }
}

@Composable
private fun BotsiLinksTextComposable(
    modifier: Modifier = Modifier,
    content: BotsiLinksText,
    style: BotsiLinksStyle?,
    staticFontSize: TextUnit? = null
) {
    val contentText = remember(content) { content.text }
    val color = remember(content) { style?.color.toColor(style?.opacity) }
    val textStyle = style?.font.toTextStyle().copy(
        fontSize = staticFontSize ?: style?.size.toFontSize()
    )

    if (!contentText.isNullOrEmpty()) {
        Text(
            modifier = modifier,
            text = contentText,
            style = textStyle,
            color = color,
        )
    }
}
