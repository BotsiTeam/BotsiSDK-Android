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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductItemContent
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toSelectedTextStyle
import com.botsi.view.utils.toShape
import com.botsi.view.utils.toTextStyle

@Composable
internal fun BotsiProductItemComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock
) {
    val content = item.content as? BotsiProductItemContent
    if (content == null) return

    val isSelected = remember(content) { content.offerState != "default" }

    val currentStyle = if (isSelected) content.selectedStyle else content.defaultStyle

    val currentText = remember(content) { content.defaultText }

    Box {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .then(currentStyle.toBorder())
                .then(currentStyle.toBackground())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!currentText?.text1.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currentText.text1,
                    style = content.defaultState?.text1.run {
                        if (isSelected) toSelectedTextStyle() else toTextStyle()
                    },
                )
            }
            if (!currentText?.text2.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currentText.text2,
                    style = content.defaultState?.text2.run {
                        if (isSelected) toSelectedTextStyle() else toTextStyle()
                    },
                )
            }
            if (!currentText?.text3.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currentText.text3,
                    style = content.defaultState?.text3.run {
                        if (isSelected) toSelectedTextStyle() else toTextStyle()
                    },
                )
            }
            if (!currentText?.text4.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currentText.text4,
                    style = content.defaultState?.text4.run {
                        if (isSelected) toSelectedTextStyle() else toTextStyle()
                    },
                )
            }
        }

        // Badge overlay if enabled
        if (content.isBadge == true && content.badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-12).dp)
                    .background(
                        color = content.badge.badgeColor.toColor(content.badge.badgeOpacity),
                        shape = RoundedCornerShape(
                            topStart = (content.badge.badgeRadius?.getOrNull(0) ?: 12).dp,
                            topEnd = (content.badge.badgeRadius?.getOrNull(1) ?: 12).dp,
                            bottomEnd = (content.badge.badgeRadius?.getOrNull(2) ?: 12).dp,
                            bottomStart = (content.badge.badgeRadius?.getOrNull(3) ?: 12).dp
                        )
                    )
                    .padding(horizontal = 8.dp)
            ) {
                content.badge.badgeText?.let { badgeText ->
                    Text(
                        text = badgeText,
                        color = content.badge.badgeTextColor.toColor(content.badge.badgeTextOpacity),
                        fontSize = (content.badge.badgeTextSize ?: 12).sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}