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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.botsi.ai.ui.model.BotsiAiUiAction
import com.botsi.ai.ui.model.BotsiAiUiState

@Composable
fun Paywall2Ui(
    modifier: Modifier = Modifier,
    activity: Activity,
    state: BotsiAiUiState,
    onAction: (BotsiAiUiAction) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Companion.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Close button
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
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            // App Title and Icon Row
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Note",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF4A4A4A)
                )
                Text(
                    text = "Plus",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF6B46C1)
                )
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Feature icons row
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.Companion.padding(horizontal = 16.dp)
            ) {
                repeat(6) { index ->
                    Box(
                        modifier = Modifier.Companion
                            .size(40.dp)
                            .background(
                                Color(0xFFE5E7EB),
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Companion.Center
                    ) {
                        // Using different icons to represent various features
                        val iconRes = when (index) {
                            0 -> R.drawable.ic_menu_edit
                            1 -> R.drawable.ic_menu_agenda
                            2 -> R.drawable.ic_menu_recent_history
                            3 -> R.drawable.ic_menu_today
                            4 -> R.drawable.ic_menu_view
                            else -> R.drawable.ic_menu_preferences
                        }
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier.Companion.size(20.dp),
                            tint = Color(0xFF6B7280)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // Features List
            Column(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Paywall2FeatureItem("Unlimited entries")
                Paywall2FeatureItem("Personal checklist & weekly goals")
                Paywall2FeatureItem("Sync with Apple, Google calendars")
                Paywall2FeatureItem("Monthly reports")
                Paywall2FeatureItem("Roll over pending to-dos and more!")
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // Subscription Plans
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.Companion.fillMaxWidth()
            ) {

                state.paywall.sourceProducts.forEach {
                    val price = it.productDetails?.subscriptionOfferDetails
                        ?.firstOrNull()?.pricingPhases?.pricingPhaseList
                        ?.firstOrNull()?.formattedPrice ?: "Free"

                    PlanOption(
                        title = it.productDetails?.title.orEmpty(),
                        subtitle = price,
                        isSelected = state.selectedProduct?.sourceProductId == it.sourceProductId,
                        onClick = { onAction(BotsiAiUiAction.SelectProduct(it)) },
                        isRecommended = true
                    )
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Subscribe Button
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
                    containerColor = Color(0xFF6B46C1)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(28.dp)
            ) {
                if (state.isLoadingButton) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Subscribe Now",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color.Companion.White
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            // Already a subscriber text
            Text(
                text = "Are you a subscriber?",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Companion.Center
            )

            Spacer(modifier = Modifier.Companion.height(8.dp))

            TextButton(
                onClick = { /* Handle restore */ }
            ) {
                Text(
                    text = "Restore purchase",
                    fontSize = 14.sp,
                    color = Color(0xFF6B46C1),
                    fontWeight = FontWeight.Companion.Medium
                )
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // Footer Links
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                TextButton(
                    onClick = { /* Handle privacy policy */ }
                ) {
                    Text(
                        text = "Privacy Policy",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF),
                        textDecoration = TextDecoration.Companion.Underline
                    )
                }

                TextButton(
                    onClick = { /* Handle terms */ }
                ) {
                    Text(
                        text = "Terms of service",
                        fontSize = 12.sp,
                        color = Color(0xFF9CA3AF),
                        textDecoration = TextDecoration.Companion.Underline
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))
        }
    }
}

@Composable
private fun Paywall2FeatureItem(
    text: String,
    modifier: Modifier = Modifier.Companion
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Check",
            tint = Color(0xFF10B981),
            modifier = Modifier.Companion.size(20.dp)
        )

        Spacer(modifier = Modifier.Companion.width(12.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            color = Color(0xFF374151),
            fontWeight = FontWeight.Companion.Medium
        )
    }
}

@Composable
private fun PlanOption(
    title: String,
    subtitle: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    isRecommended: Boolean = false,
    modifier: Modifier = Modifier.Companion
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = if (isSelected) Color(0xFFF3F4F6) else Color.Companion.Transparent,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            )
            .run {
                if (isSelected) {
                    border(
                        width = 2.dp,
                        color = Color(0xFF6B46C1),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                } else {
                    border(
                        width = 1.dp,
                        color = Color(0xFFE5E7EB),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    )
                }
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.Companion.fillMaxSize(),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Companion.Bold else FontWeight.Companion.Medium,
                    color = Color(0xFF1F2937)
                )

                subtitle?.let {
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            // Radio button
            Box(
                modifier = Modifier.Companion
                    .size(24.dp)
                    .background(
                        color = if (isSelected) Color(0xFF6B46C1) else Color.Companion.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color(0xFF6B46C1) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Companion.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier.Companion
                            .size(8.dp)
                            .background(
                                color = Color.Companion.White,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}