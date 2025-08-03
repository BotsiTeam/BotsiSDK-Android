package com.botsi.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.botsi.Botsi
import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct
import kotlinx.coroutines.launch

class BotsiFragment : Fragment() {

    companion object {
        fun newInstance() = BotsiFragment()
    }

    private val app: BotsiApp
        get() = requireContext().applicationContext as BotsiApp

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(context = requireContext()).apply {
            setContent {
                MaterialTheme {
                    ContentUi()
                }
            }
        }
    }

    @Composable
    private fun ContentUi() {
        var products by remember { mutableStateOf<List<BotsiProduct>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var isSuccessPayment by remember { mutableStateOf(false) }
        var isError by remember { mutableStateOf(false) }
        var selectedSub by remember { mutableStateOf<BotsiProduct?>(null) }

        LaunchedEffect(Unit) {
            Botsi.getPaywall(
                placementId = app.botsiStorage.placementId,
                successCallback = {
                    Botsi.getPaywallProducts(
                        paywall = it,
                        successCallback = { result ->
                            isLoading = false
                            products = result
                        },
                        errorCallback = {
                            isError = true
                            isLoading = false
                        },
                    )
                },
                errorCallback = {
                    isError = true
                    isLoading = false
                }
            )
        }

        val mainColor = Color(0xFFFFFFFF)
        val textColor = Color(0xFF222222)

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            containerColor = mainColor,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
        ) {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    painter = painterResource(R.drawable.bg),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box {
                        Image(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .size(200.dp),
                            painter = painterResource(R.drawable.diamond),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )

                        Image(
                            modifier = Modifier
                                .size(60.dp)
                                .align(Alignment.Center)
                                .offset(x = 30.dp, y = 30.dp),
                            painter = painterResource(R.drawable.glitter),
                            contentDescription = null,
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 0.dp),
                        text = "Premium plan",
                        color = textColor,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Text(
                        modifier = Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 48.dp,
                        ),
                        text = "Subscribe to have full access to all premium features",
                        color = textColor,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        modifier = Modifier
                            .padding(
                                vertical = 8.dp,
                                horizontal = 48.dp,
                            )
                            .clickable(
                                onClick = {
                                    isLoading = true
                                    Botsi.restorePurchase(
                                        successCallback = {
                                            isLoading = false
                                            lifecycleScope.launch {
                                                snackbarHostState.showSnackbar("Success")
                                            }
                                        },
                                        errorCallback = {
                                            isLoading = false
                                            isError = true
                                        }
                                    )
                                },
                            ),
                        text = "Restore",
                        color = textColor,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.height(48.dp))

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(top = 24.dp)
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally),
                        )
                    } else {
                        if (!isSuccessPayment) {
                            products.forEach { product ->
                                SubscriptionItem(
                                    modifier = Modifier.padding(
                                        vertical = 6.dp, horizontal = 24.dp
                                    ),
                                    title = product.name,
                                    description = product.title,
                                    price = product.subscriptionOffer?.pricingPhases?.firstOrNull()?.formattedPrice.orEmpty(),
                                    textColor = textColor,
                                    selected = selectedSub?.basePlanId == product.basePlanId,
                                    onClick = {
                                        selectedSub = if (selectedSub?.basePlanId != product.basePlanId ||
                                            selectedSub?.subscriptionOffer?.offerId != product.subscriptionOffer?.offerId
                                        ) {
                                            product
                                        } else {
                                            null
                                        }
                                    },
                                )
                            }
                        } else {
                            if (isError) {
                                Text(text = "Error occurred")
                            } else {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Image(
                                        modifier = Modifier.size(80.dp),
                                        painter = painterResource(R.drawable.success),
                                        contentDescription = null
                                    )
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            end = 24.dp,
                                            top = 18.dp,
                                        ), text = "Subscription success", fontSize = 18.sp, style = TextStyle(
                                            fontWeight = FontWeight.Medium
                                        ), color = textColor
                                    )
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            end = 24.dp,
                                            top = 8.dp,
                                        ), text = "Than you for choosing us!", fontSize = 14.sp, style = TextStyle(
                                            fontWeight = FontWeight.Normal
                                        ), color = Color(0xFF9B9EA1)
                                    )
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 24.dp,
                                            end = 24.dp,
                                            top = 8.dp,
                                        ), text = "Your plan", fontSize = 16.sp, style = TextStyle(
                                            fontWeight = FontWeight.Normal
                                        ), color = textColor
                                    )
                                    Spacer(Modifier.height(4.dp))

                                    selectedSub?.let { sub ->
                                        SubscriptionItem2(
                                            modifier = Modifier.padding(
                                                vertical = 6.dp, horizontal = 48.dp
                                            ),
                                            title = sub.name,
                                            description = sub.name,
                                            textColor = textColor,
                                        )
                                    }

                                }
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isSuccessPayment) {
                        Button(
                            modifier = Modifier
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = 8.dp
                                )
                                .fillMaxWidth(),
                            enabled = selectedSub != null,
                            contentPadding = PaddingValues(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xffFF9542), disabledContainerColor = Color(0xFFFFCDA6)
                            ),
                            onClick = {
                                selectedSub?.let { sub ->
                                    isLoading = true
                                    Botsi.makePurchase(
                                        activity = requireActivity(),
                                        product = sub,
                                        callback = {
                                            isSuccessPayment = true
                                            isLoading = false
                                            lifecycleScope.launch {
                                                snackbarHostState.showSnackbar("Success")
                                            }
                                        },
                                        errorCallback = {
                                            isLoading = false
                                            isError = false
                                        }
                                    )
                                }
                            },
                        ) {
                            Text(
                                text = "Subscribe",
                                style = TextStyle(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SubscriptionItem(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
        price: String,
        textColor: Color,
        selected: Boolean,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFFAFBFA),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onClick)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.widthIn(max = 150.dp)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        color = textColor,
                        lineHeight = 16.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        lineHeight = 14.sp,
                        color = Color(0xFF9B9EA1)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor,
                    textAlign = TextAlign.End,
                )
                RadioButton(
                    selected = selected,
                    onClick = onClick
                )
            }
        }
    }

    @Composable
    fun SubscriptionItem2(
        modifier: Modifier = Modifier,
        title: String,
        description: String,
        textColor: Color,
    ) {
        Column(
            modifier = modifier
                .background(
                    color = Color(0xFFFAFBFA), shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.widthIn(max = 150.dp),
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 22.sp,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF9B9EA1)
            )
        }
    }
}
