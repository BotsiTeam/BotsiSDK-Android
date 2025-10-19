package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.utils.toBorderStroke
import com.botsi.view.utils.toBrush
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@Composable
internal fun BotsiButtonComposable(
    modifier: Modifier = Modifier,
    buttonContent: BotsiButtonContent,
    onClick: () -> Unit,
) {
    val outerPaddings = remember(buttonContent) { buttonContent.toPaddings() }
    val innerPaddings = remember(buttonContent) { buttonContent.contentLayout.toPaddings() }
    val verticalOffset = remember(buttonContent) { (buttonContent.verticalOffset ?: 0).dp }
    val buttonColor = remember(buttonContent) {
        buttonContent.style?.fillColor.toBrush(
            buttonContent.style?.opacity
        )
    }
    val buttonBorder = remember(buttonContent) { buttonContent.style.toBorderStroke() }
    val buttonShape = remember(buttonContent) { buttonContent.style.toShape() }
    val buttonContentAlignment = remember(buttonContent) {
        when (buttonContent.contentLayout?.align) {
            BotsiAlign.Left -> Alignment.Start
            BotsiAlign.Right -> Alignment.End
            BotsiAlign.Center -> Alignment.CenterHorizontally
            else -> Alignment.Start
        }
    }

    Box(
        modifier = modifier
            .padding(outerPaddings)
            .fillMaxWidth()
            .background(
                brush = buttonColor,
                shape = buttonShape,
            )
            .border(buttonBorder)
            .clickable(onClick = onClick)
            .padding(innerPaddings)
            .offset(y = verticalOffset),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = buttonContentAlignment,
            verticalArrangement = Arrangement.Center,
        ) {
            buttonContent.text?.let { text ->
                if (!text.text.isNullOrEmpty()) {
                    BotsiTextComposable(
                        modifier = modifier,
                        text = text,
                        fullWidth = false
                    )
                }
            }
            buttonContent.secondaryText?.let { text ->
                if (!text.text.isNullOrEmpty()) {
                    BotsiTextComposable(
                        modifier = modifier,
                        text = text,
                        fullWidth = false
                    )
                }
            }
        }
    }
}