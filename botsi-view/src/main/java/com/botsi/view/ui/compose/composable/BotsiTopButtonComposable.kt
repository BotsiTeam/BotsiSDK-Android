package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.botsi.view.R
import com.botsi.view.model.content.BotsiButtonIconType
import com.botsi.view.model.content.BotsiButtonType
import com.botsi.view.model.content.BotsiTopButton
import com.botsi.view.utils.clipToShape
import com.botsi.view.utils.toAlignment
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor

@Composable
internal fun BoxScope.BotsiTopButtonComposable(
    modifier: Modifier = Modifier,
    topButton: BotsiTopButton,
    topButtonClick: (BotsiTopButton) -> Unit
) {
    if (topButton.enabled == true) {
        when (topButton.buttonType) {
            BotsiButtonType.Icon -> {
                val painter: Painter? = when (topButton.icon?.type) {
                    BotsiButtonIconType.Close -> painterResource(R.drawable.ic_close_24)
                    else -> null
                }
                painter?.let {
                    Icon(
                        modifier = modifier
                            .size(32.dp)
                            .align(topButton.buttonAlign.toAlignment())
                            .then(topButton.style.clipToShape())
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

            }

            else -> {}
        }
    }
}