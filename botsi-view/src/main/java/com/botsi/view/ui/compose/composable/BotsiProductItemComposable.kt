package com.botsi.view.ui.compose.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.view.model.content.BotsiAlign
import com.botsi.view.model.content.BotsiPaywallBlock
import com.botsi.view.model.content.BotsiProductItemContent
import com.botsi.view.model.content.BotsiProductState
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.utils.toAlignmentHorizontal
import com.botsi.view.utils.toBackground
import com.botsi.view.utils.toBorder
import com.botsi.view.utils.toColor
import com.botsi.view.utils.toSelectedTextStyle
import com.botsi.view.utils.toTextStyle

@Composable
internal fun BotsiProductItemComposable(
    modifier: Modifier = Modifier,
    item: BotsiPaywallBlock,
    align: BotsiAlign?,
    isHorizontal: Boolean,
    selectedProductId: Long?,
    onAction: (BotsiPaywallUiAction) -> Unit,
) {
    val content = item.content as? BotsiProductItemContent
    if (content == null) return

    val isSelectedStateToDraw = remember(content.state) {
        content.state == BotsiProductState.Selected
    }
    LaunchedEffect(content) {
        if (isSelectedStateToDraw) {
            item.meta?.productId?.let {
                onAction(
                    BotsiPaywallUiAction.ProductSelected(
                        productId = it
                    )
                )
            }
        }
    }
    val isProductSelected =
        remember(selectedProductId) {
            (item.meta?.productId == null && isSelectedStateToDraw) ||
                    (item.meta?.productId != null && selectedProductId == item.meta.productId)
        }

    val currentStyle = if (isProductSelected) content.selectedStyle else content.defaultStyle

    val currentText = remember(content) { content.defaultText }

    Box(
        modifier = modifier
            .run {
                if (isHorizontal) {
                    fillMaxHeight()
                } else {
                    this
                }
            }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .run {
                    if (isHorizontal) {
                        fillMaxHeight()
                    } else {
                        this
                    }
                }
                .then(currentStyle.toBorder())
                .then(currentStyle.toBackground())
                .clickable(onClick = {
                    onAction(
                        BotsiPaywallUiAction.ProductSelected(
                            productId = item.meta?.productId
                        )
                    )
                })
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = align.toAlignmentHorizontal()
        ) {
            if (!currentText?.text1.isNullOrEmpty()) {
                Text(
                    modifier = Modifier,
                    text = currentText.text1,
                    style = if (isProductSelected) {
                        content.selectedState?.text1.toSelectedTextStyle()
                    } else {
                        content.defaultState?.text1.toTextStyle()
                    },
                )
            }
            if (!currentText?.text2.isNullOrEmpty()) {
                Text(
                    modifier = Modifier,
                    text = currentText.text2,
                    style = if (isProductSelected) {
                        content.selectedState?.text2.toSelectedTextStyle()
                    } else {
                        content.defaultState?.text2.toTextStyle()
                    },
                )
            }
            if (!currentText?.text3.isNullOrEmpty()) {
                Text(
                    modifier = Modifier,
                    text = currentText.text3,
                    style = if (isProductSelected) {
                        content.selectedState?.text3.toSelectedTextStyle()
                    } else {
                        content.defaultState?.text3.toTextStyle()
                    },
                )
            }
            if (!currentText?.text4.isNullOrEmpty()) {
                Text(
                    modifier = Modifier,
                    text = currentText.text4,
                    style = if (isProductSelected) {
                        content.selectedState?.text4.toSelectedTextStyle()
                    } else {
                        content.defaultState?.text4.toTextStyle()
                    },
                )
            }
        }

        // Badge overlay if enabled
        if (content.isBadge == true && content.badge != null) {
            Box(
                modifier = Modifier
                    .align(if (align == BotsiAlign.Column) Alignment.TopEnd else Alignment.TopCenter)
                    .offset(y = (-12).dp)
                    .then(content.badge.toBackground())
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