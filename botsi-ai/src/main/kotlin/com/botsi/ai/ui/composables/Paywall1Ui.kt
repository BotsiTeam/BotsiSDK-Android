package com.botsi.ai.ui.composables

import android.R
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.botsi.ai.ui.model.BotsiAiUiAction
import com.botsi.ai.ui.model.BotsiAiUiState

@Composable
fun Paywall1Ui(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: BotsiAiUiState,
    onAction: (BotsiAiUiAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color(0xFFeaf3ff)
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_menu_close_clear_cancel),
            contentDescription = "Close",
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.Companion.TopEnd)
                .clickable(onClick = { onAction(BotsiAiUiAction.Back) }),
            tint = Color(0xFF666666)
        )

        Column(
            modifier = Modifier.Companion
                .padding(top = 32.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Become a Premium",
                fontSize = 22.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF1A202C),
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            AsyncImage(
                modifier = Modifier.Companion.size(200.dp),
                model = "https://cdn-icons-png.flaticon.com/512/3649/3649801.png",
                contentDescription = null,
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // Features List
            Paywall1FeatureItem(
                title = "All Locations",
                description = "Connect through any of our 97 locations all around the world with unparalleled anonymity"
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Paywall1FeatureItem(
                title = "Top Speed",
                description = "Don't let safety in the way of enjoying media & content at the highest level of quality"
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Paywall1FeatureItem(
                title = "No Ads",
                description = "Get rid of all those banners and videos when you open the app"
            )

            Spacer(modifier = Modifier.Companion.height(36.dp))

            state.paywall.sourceProducts.forEach {
                Button(
                    onClick = { onAction(BotsiAiUiAction.SelectProduct(it)) },
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Companion.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    val isSelected =
                        state.selectedProduct?.sourceProductId == it.sourceProductId

                    Box(
                        modifier = Modifier.Companion
                            .fillMaxSize()
                            .run {
                                if (isSelected) {
                                    background(
                                        brush = Brush.Companion.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFF38E54D),
                                                Color(0xFF4FD1C7)
                                            )
                                        ),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                            30.dp
                                        )
                                    )
                                } else {
                                    this
                                }
                            },
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        val price = it.productDetails?.subscriptionOfferDetails
                            ?.firstOrNull()?.pricingPhases?.pricingPhaseList
                            ?.firstOrNull()?.formattedPrice ?: "Free"

                        Text(
                            text = "${it.productDetails?.title.orEmpty()} $price",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Companion.Bold,
                            color = if (isSelected) Color.Companion.White else Color(
                                0xFF2F2F2F
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.Companion.height(8.dp))
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (!state.isLoadingButton) {
                        onAction(BotsiAiUiAction.Pay(activity))
                    }
                }
            ) {
                if (state.isLoadingButton) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Continue",
                        color = Color(0xFF000000),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Companion.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Restore Purchases
            TextButton(
                onClick = { /* Handle restore purchases */ }
            ) {
                Text(
                    text = "Restore Purchases",
                    color = Color(0xFF6B7280),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Companion.Medium
                )
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Terms Text
            Text(
                text = "*Subscriptions will automatically renew and your credit card will be charged at the end of each period, unless auto-renew is turned off at least 24 hours before the end of the current period. Cancel at any time in your iTunes account settings.",
                fontSize = 11.sp,
                color = Color(0xFF9CA3AF),
                textAlign = TextAlign.Companion.Center,
                lineHeight = 15.sp,
                modifier = Modifier.Companion.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.Companion.height(32.dp))
        }
    }
}

@Composable
private fun Paywall1FeatureItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier.Companion
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.Top
    ) {
        // Green checkmark with enhanced styling
        Box(
            modifier = Modifier.Companion
                .size(28.dp)
                .background(
                    brush = Brush.Companion.linearGradient(
                        colors = listOf(
                            Color(0xFF38E54D),
                            Color(0xFF4FD1C7)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Companion.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Check",
                tint = Color.Companion.White,
                modifier = Modifier.Companion.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.Companion.width(18.dp))

        Column(
            modifier = Modifier.Companion.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF1A202C)
            )

            Spacer(modifier = Modifier.Companion.height(6.dp))

            Text(
                text = description,
                fontSize = 15.sp,
                color = Color(0xFF6B7280),
                lineHeight = 22.sp
            )
        }
    }
}