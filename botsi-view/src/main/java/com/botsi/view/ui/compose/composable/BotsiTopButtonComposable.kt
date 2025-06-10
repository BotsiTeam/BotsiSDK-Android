package com.botsi.view.ui.compose.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.botsi.view.R
import com.botsi.view.model.content.BotsiButtonIconType
import com.botsi.view.model.content.BotsiButtonType
import com.botsi.view.model.content.BotsiTopButton
import com.botsi.view.utils.toAlignment
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toShape
import kotlinx.coroutines.delay

@Composable
internal fun BotsiTopButtonComposable(
    modifier: Modifier = Modifier,
    topButton: BotsiTopButton,
    topButtonClick: (BotsiTopButton) -> Unit
) {
    if (topButton.enabled == true) {
        val delayMillis = remember(topButton.delay) { topButton.delay ?: 0 }
        var isVisible by remember(delayMillis) {
            mutableStateOf(false)
        }

        LaunchedEffect(topButton.delay) {
            if (delayMillis > 0) {
                delay(delayMillis)
            }
            isVisible = true
        }

        AnimatedVisibility(visible = isVisible, enter = fadeIn(), exit = fadeOut()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                when (topButton.buttonType) {
                    BotsiButtonType.Icon -> {
                        val painter: Painter? = when (topButton.icon?.type) {
                            BotsiButtonIconType.Close -> painterResource(R.drawable.ic_close_24)
                            BotsiButtonIconType.Next -> painterResource(R.drawable.ic_next_24)
                            BotsiButtonIconType.Prev -> painterResource(R.drawable.ic_prev_24)
                            else -> null
                        }
                        painter?.let {
                            Icon(
                                modifier = Modifier
                                    .size(32.dp)
                                    .align(topButton.buttonAlign.toAlignment())
                                    .clip(topButton.style.toShape())
                                    .then(topButton.style.toBackground())
                                    .then(topButton.style.toBorder())
                                    .clickable(onClick = { topButtonClick(topButton) })
                                    .padding(8.dp),
                                painter = it,
                                contentDescription = topButton.actionId,
                                tint = topButton.icon?.color.toColor(),
                            )
                        }
                    }

                    BotsiButtonType.Text -> {
                        topButton.text?.let {
                            BotsiTextComposable(
                                modifier = Modifier
                                    .wrapContentHeight()
                                    .align(topButton.buttonAlign.toAlignment())
                                    .clip(topButton.style.toShape())
                                    .then(topButton.style.toBackground())
                                    .then(topButton.style.toBorder())
                                    .clickable(onClick = { topButtonClick(topButton) })
                                    .padding(vertical = 4.dp, horizontal = 16.dp),
                                text = it,
                                maxLines = 1,
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}