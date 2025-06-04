package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiButtonContent
import com.botsi.view.utils.toBorderStroke
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toPaddings
import com.botsi.view.utils.toShape

@Composable
internal fun BotsiButtonComposable(
    modifier: Modifier = Modifier,
    buttonContent: BotsiButtonContent,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(buttonContent.toPaddings())
            .fillMaxWidth()
            .padding(buttonContent.contentLayout.toPaddings())
            .offset(y = (buttonContent.verticalOffset ?: 0).dp),
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            containerColor = buttonContent.style?.color.toColor(
                buttonContent.style?.opacity
            ),
        ),
        border = buttonContent.style.toBorderStroke(),
        shape = buttonContent.style.toShape()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = when (buttonContent.contentLayout?.align) {
                BotsiAlign.Left -> Alignment.Start
                BotsiAlign.Right -> Alignment.End
                BotsiAlign.Center -> Alignment.CenterHorizontally
                else -> Alignment.Start
            },
            verticalArrangement = Arrangement.Center,
        ) {
            buttonContent.text?.let { text ->
                if (!text.text.isNullOrEmpty()) {
                    BotsiTextComposable(
                        modifier = modifier,
                        text = text,
                    )
                }
            }
            buttonContent.secondaryText?.let { text ->
                if (!text.text.isNullOrEmpty()) {
                    BotsiTextComposable(
                        modifier = modifier,
                        text = text,
                    )
                }
            }
        }
    }
}