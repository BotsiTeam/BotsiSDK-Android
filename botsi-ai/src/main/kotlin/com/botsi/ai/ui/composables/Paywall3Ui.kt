package com.botsi.ai.ui.composables

import android.R
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.ai.ui.model.BotsiAiUiAction
import com.botsi.ai.ui.model.BotsiAiUiState

@Composable
fun Paywall3Ui(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: BotsiAiUiState,
    onAction: (BotsiAiUiAction) -> Unit
) {
    LaunchedEffect(Unit) {
        onAction(BotsiAiUiAction.View)
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Close button
        Icon(
            painter = painterResource(R.drawable.ic_menu_close_clear_cancel),
            contentDescription = "Close",
            modifier = Modifier.Companion
                .padding(16.dp)
                .size(24.dp)
                .align(Alignment.Companion.TopEnd)
                .clickable(onClick = { onAction(BotsiAiUiAction.Back) }),
            tint = Color(0xFF6B7280)
        )

        Column(
            modifier = Modifier.Companion
                .padding(top = 60.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {

            // Hero Illustration - Beautiful flower
            Box(
                modifier = Modifier.Companion
                    .size(180.dp)
                    .background(
                        brush = Brush.Companion.radialGradient(
                            colors = listOf(
                                Color(0xFFE0E7FF),
                                Color(0xFFC7D2FE),
                                Color(0xFFA5B4FC)
                            ),
                            radius = 200f
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Companion.Center
            ) {
                // Flower illustration placeholder
                Box(
                    modifier = Modifier.Companion
                        .size(120.dp)
                        .background(
                            brush = Brush.Companion.linearGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6),
                                    Color(0xFFA855F7),
                                    Color(0xFFC084FC)
                                )
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text(
                        text = "🌸",
                        fontSize = 60.sp
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // Title
            Text(
                text = "Unlock Premium",
                fontSize = 28.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // Features List
            Column(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Paywall3FeatureItem(
                    text = "Unlimited image generations"
                )
                Paywall3FeatureItem(
                    text = "High-quality downloads"
                )
                Paywall3FeatureItem(
                    text = "No watermarks"
                )
            }

            Spacer(modifier = Modifier.Companion.height(40.dp))

            // Pricing Cards Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                modifier = Modifier.Companion.fillMaxWidth(),
            ) {
                state.paywall.sourceProducts.forEach {
                    val price = it.productDetails?.subscriptionOfferDetails
                        ?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull()?.formattedPrice ?: "Free"

                    val period = it.productDetails?.subscriptionOfferDetails
                        ?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull()?.billingPeriod.orEmpty()

                    PricingCard3(
                        modifier = Modifier.Companion.weight(1f),
                        title = it.productDetails?.title.orEmpty(),
                        price = price,
                        period = period,
                        isSelected = state.selectedProduct?.sourceProductId == it.sourceProductId,
                        onClick = { onAction(BotsiAiUiAction.SelectProduct(it)) },
                        isHighlighted = state.selectedProduct?.sourceProductId == "yearly"
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // Trial text
            Text(
                text = "Try 7 days for free. Cancel anytime.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(20.dp))

            // Start Free Trial Button
            Button(
                onClick = {
                    if (!state.isLoadingButton) {
                        onAction(BotsiAiUiAction.Pay(activity))
                    }
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                if (state.isLoadingButton) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Start Free Trial",
                        fontSize = 18.sp,
                        color = Color.Companion.White
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(20.dp))

            // Footer Links
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { onAction(BotsiAiUiAction.Restore(activity)) }
                ) {
                    Text(
                        text = "Restore Purchases",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }

                TextButton(
                    onClick = { /* Handle terms */ }
                ) {
                    Text(
                        text = "Terms & Privacy",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))
        }
    }
}

@Composable
private fun Paywall3FeatureItem(
    text: String,
    modifier: Modifier = Modifier.Companion
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Icon background circle
        Box(
            modifier = Modifier.Companion
                .size(32.dp)
                .background(
                    color = Color(0xFF6366F1),
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

        Spacer(modifier = Modifier.Companion.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Companion.Light
        )
    }
}

@Composable
private fun PricingCard3(
    title: String,
    price: String,
    period: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier.Companion
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        // Card
        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(
                    color = if (isSelected) {
                        if (isHighlighted) Color(0xFFF0F8FF) else Color(0xFFF9FAFB)
                    } else Color.Companion.White,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                )
                .run {
                    if (isSelected) {
                        border(
                            width = 2.dp,
                            color = if (isHighlighted) Color(0xFF6366F1) else Color(0xFF9CA3AF),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                    } else {
                        border(
                            width = 1.dp,
                            color = Color(0xFFE5E7EB),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        )
                    }
                }
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                if (badge != null) {
                    Spacer(modifier = Modifier.Companion.height(8.dp))
                }

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Companion.Light,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                Text(
                    text = price,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF1F2937)
                )

                Text(
                    text = period,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Companion.Light,
                    color = Color(0xFF6B7280)
                )
            }
        }

        // Badge
        badge?.let {
            Box(
                modifier = Modifier.Companion
                    .align(Alignment.Companion.TopCenter)
                    .offset(y = (-8).dp)
                    .background(
                        color = Color(0xFF6366F1),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(48.dp)
                    )
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Companion.Light,
                    color = Color.Companion.White
                )
            }
        }
    }
}