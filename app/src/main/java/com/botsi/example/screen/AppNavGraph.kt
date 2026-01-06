package com.botsi.example.screen

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.botsi.Botsi
import com.botsi.ai.ui.composables.BotsiAiEntryPoint
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.example.BotsiApp
import com.botsi.view.BotsiViewConfig
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.timer.BotsiTimerResolver
import com.botsi.view.ui.compose.entry_point.BotsiPaywallEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Date

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "projects") {
        composable("projects") { ProjectsScreen(navController) }
        composable("setup/{destination}") { backStackEntry ->
            val destination = backStackEntry.arguments?.getString("destination") ?: "classic"
            SetupScreen(navController, destination)
        }
        composable("classic/{clearCache}") { backStackEntry ->
            val clearCache = backStackEntry.arguments?.getString("clearCache")
                ?.toBooleanStrictOrNull() ?: false
            ClassicScreen(navController, clearCache)
        }
        composable("ui/{clearCache}") { backStackEntry ->
            val clearCache = backStackEntry.arguments?.getString("clearCache")
                ?.toBooleanStrictOrNull() ?: false
            UiPaywallScreen(clearCache)
        }
        composable("ai") { AiScreen(navController) }
    }
}

@Composable
private fun BotsiLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Circle icon
        Image(
            painter = painterResource(com.botsi.example.R.drawable.ic_launcher_foreground),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        // "botsi" text
        Text(
            text = "botsi",
            color = Color.White,
            fontSize = 32.sp,
            style = TextStyle(fontWeight = FontWeight.Light)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectsScreen(navController: NavHostController) {
    // Dark theme colors
    val backgroundDark = Color(0xFF081722)
    val containerDark = Color(0xFF2a304a)
    val textPrimary = Color.White

    data class Project(
        val name: String,
        val icon: String,
        val route: String
    )

    val projects = listOf(
        Project("Botsi Classic", "C", "classic"),
        Project("Botsi UI Paywall", "U", "ui"),
        Project("Botsi AI", "A", "ai")
    )

    Scaffold(
        containerColor = backgroundDark,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            BotsiLogo(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                text = "Projects",
                color = textPrimary,
                fontSize = 34.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = containerDark
            ) {
                Column {
                    projects.forEachIndexed { index, project ->
                        ProjectItem(
                            name = project.name,
                            icon = project.icon,
                            onClick = { navController.navigate("setup/${project.route}") }
                        )
                        if (index < projects.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(start = 72.dp),
                                color = Color(0xFF38383A),
                                thickness = 0.5.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectItem(
    name: String,
    icon: String,
    onClick: () -> Unit
) {
    val textPrimary = Color.White
    val iconBackground = Color(0xFF019cfe)

    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = RoundedCornerShape(10.dp),
                color = iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = icon,
                        color = textPrimary,
                        fontSize = 20.sp,
                        style = TextStyle(fontWeight = FontWeight.SemiBold)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                text = name,
                color = textPrimary,
                fontSize = 17.sp,
                style = TextStyle(fontWeight = FontWeight.Medium)
            )

            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color(0xFF3C3C3E)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetupScreen(navController: NavHostController, destinationRoute: String) {
    val context = LocalContext.current
    val app = remember { context.applicationContext as BotsiApp }

    var appKey by remember { mutableStateOf(TextFieldValue(app.botsiStorage.appKey)) }
    var secretKey by remember { mutableStateOf(TextFieldValue(app.botsiStorage.secretKey)) }
    var placementId by remember { mutableStateOf(TextFieldValue(app.botsiStorage.placementId)) }
    var isLoading by remember { mutableStateOf(false) }

    // Map route to display name
    val destinationName = when (destinationRoute) {
        "classic" -> "Botsi Classic"
        "ui" -> "Botsi UI Paywall"
        "ai" -> "Botsi AI"
        else -> "Botsi Classic"
    }

    fun saveStorage() {
        app.botsiStorage.appKey = appKey.text
        app.botsiStorage.secretKey = secretKey.text
        app.botsiStorage.placementId = placementId.text
    }

    // Dark theme colors
    val backgroundDark = Color(0xFF081722)
    val containerDark = Color(0xFF3a405a)
    val accentBlue = Color(0xFF0EA5E9)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF9CA3AF)
    val borderColor = Color(0xFF4B5563)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundDark,
                    titleContentColor = textPrimary
                ),
                title = {
                    Text(
                        text = destinationName,
                        style = TextStyle(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = accentBlue
                        )
                    }
                }
            )
        },
        containerColor = backgroundDark,
        bottomBar = {
            Button(
                modifier = Modifier
                    .imePadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentBlue,
                    disabledContainerColor = accentBlue.copy(alpha = 0.5f)
                ),
                onClick = {
                    val clearCache = app.botsiStorage.appKey != appKey.text
                    saveStorage()

                    when (destinationRoute) {
                        "ai" -> navigateOnUi(navController, "ai")
                        "ui" -> navigateOnUi(navController, "ui", clearCache)

                        else -> {
                            // Classic: navigate and let ClassicScreen handle activation
                            navigateOnUi(navController, "classic", clearCache)
                        }
                    }
                }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text(
                        text = "Start",
                        style = TextStyle(fontWeight = FontWeight.Medium),
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            BotsiLogo(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            if (destinationRoute == "classic" || destinationRoute == "ui") {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onValueChange = { appKey = it },
                    label = {
                        Text(
                            text = "App key",
                            style = TextStyle(fontWeight = FontWeight.Medium)
                        )
                    },
                    value = appKey,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = containerDark,
                        unfocusedContainerColor = containerDark,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedLabelColor = accentBlue,
                        unfocusedLabelColor = textSecondary,
                        focusedIndicatorColor = accentBlue,
                        unfocusedIndicatorColor = borderColor,
                        cursorColor = accentBlue
                    )
                )
            }

            if (destinationRoute == "ai") {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onValueChange = { secretKey = it },
                    label = {
                        Text(
                            text = "Secret key",
                            style = TextStyle(fontWeight = FontWeight.Medium)
                        )
                    },
                    value = secretKey,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = containerDark,
                        unfocusedContainerColor = containerDark,
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedLabelColor = accentBlue,
                        unfocusedLabelColor = textSecondary,
                        focusedIndicatorColor = accentBlue,
                        unfocusedIndicatorColor = borderColor,
                        cursorColor = accentBlue
                    )
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                onValueChange = { placementId = it },
                label = {
                    Text(
                        text = "Placement id",
                        style = TextStyle(fontWeight = FontWeight.Medium)
                    )
                },
                value = placementId,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerDark,
                    unfocusedContainerColor = containerDark,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary,
                    focusedLabelColor = accentBlue,
                    unfocusedLabelColor = textSecondary,
                    focusedIndicatorColor = accentBlue,
                    unfocusedIndicatorColor = borderColor,
                    cursorColor = accentBlue
                )
            )
        }
    }
}

@Composable
private fun AiScreen(navController: NavHostController) {
    val context = LocalContext.current
    val app = remember { context.applicationContext as BotsiApp }
    MaterialTheme {
        BotsiAiEntryPoint(
            activity = context.findActivity(),
            placementId = app.botsiStorage.placementId,
            secretKey = app.botsiStorage.secretKey,
            onBack = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UiPaywallScreen(clearCache: Boolean) {
    val context = LocalContext.current
    val app = remember { context.applicationContext as BotsiApp }

    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<Throwable?>(null) }
    var state by remember { mutableStateOf(UiState()) }

    // Dark theme colors
    val backgroundDark = Color(0xFF1a1c34)
    val accentBlue = Color(0xFF0EA5E9)
    val textPrimary = Color.White

    LaunchedEffect(Unit) {
        Botsi.activate(
            context = context,
            apiKey = app.botsiStorage.appKey,
            clearCache = clearCache,
            successCallback = {
                Botsi.getPaywall(
                    placementId = app.botsiStorage.placementId,
                    successCallback = { paywall ->
                        Botsi.getPaywallProducts(
                            paywall = paywall,
                            successCallback = { products ->
                                state = UiState(paywall = paywall, products = products)
                                isLoading = false
                            },
                            errorCallback = { e -> error = e; isLoading = false }
                        )
                    },
                    errorCallback = { e -> error = e; isLoading = false }
                )
            },
            errorCallback = { e ->
                isLoading = false
                context.toast(e.message.orEmpty())
            }
        )
    }

    Scaffold(containerColor = backgroundDark) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accentBlue)
            }
        } else if (error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${error?.message ?: "unknown"}", color = textPrimary)
            }
        } else {
            MaterialTheme {
                BotsiPaywallEntryPoint(
                    viewConfig = BotsiViewConfig(
                        paywall = state.paywall,
                        products = state.products,
                    ),
                    timerResolver = object : BotsiTimerResolver {
                        override fun timerEndAtDate(timerId: String): Date {
                            val durationMillis = parseTimeString(timerId)
                            return Date(System.currentTimeMillis() + durationMillis)
                        }

                        private fun parseTimeString(timeString: String): Long {
                            if (timeString.isBlank()) return 0L
                            return try {
                                val cleanedTime = timeString.trim().lowercase()
                                var totalMilliseconds = 0L
                                Regex("""(\d+)d""").find(cleanedTime)
                                    ?.let { m -> totalMilliseconds += m.groupValues[1].toInt() * 86400 * 1000L }
                                Regex("""(\d+)h""").find(cleanedTime)
                                    ?.let { m -> totalMilliseconds += m.groupValues[1].toInt() * 3600 * 1000L }
                                Regex("""(\d+)m""").find(cleanedTime)
                                    ?.let { m -> totalMilliseconds += m.groupValues[1].toInt() * 60 * 1000L }
                                Regex("""(\d+)s""").find(cleanedTime)
                                    ?.let { m -> totalMilliseconds += m.groupValues[1].toInt() * 1000L }
                                if (totalMilliseconds == 0L) cleanedTime.toIntOrNull()
                                    ?.let { it * 1000L } ?: 0L else totalMilliseconds
                            } catch (_: Exception) {
                                3600 * 1000L
                            }
                        }
                    },
                    eventHandler = object : BotsiPublicEventHandler {
                        override fun onLoginAction() {
                            context.toast("Login action clicked")
                        }

                        override fun onCustomAction(actionId: String) {
                            context.toast("$actionId clicked")
                        }

                        override fun onSuccessRestore(profile: com.botsi.domain.model.BotsiProfile) {
                            context.toast("Restore success")
                        }

                        override fun onErrorRestore(error: Throwable) {
                            context.toast("Restore error ${error.message}")
                        }

                        override fun onSuccessPurchase(
                            profile: com.botsi.domain.model.BotsiProfile,
                            purchase: com.botsi.domain.model.BotsiPurchase
                        ) {
                            context.toast("Purchase success")
                        }

                        override fun onErrorPurchase(error: Throwable) {
                            context.toast("Purchase error ${error.message}")
                        }

                        override fun onTimerEnd(actionId: String) {
                            context.toast("Timer end $actionId")
                        }

                        override fun onAwaitSubscriptionsParams(product: BotsiProduct): BotsiSubscriptionUpdateParameters? {
                            return null
                        }
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassicScreen(navController: NavHostController, clearCache: Boolean) {
    val context = LocalContext.current
    val app = remember { context.applicationContext as BotsiApp }

    var products by remember { mutableStateOf<List<BotsiProduct>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isSuccessPayment by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var selectedSub by remember { mutableStateOf<com.botsi.domain.model.BotsiProduct?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Activate Botsi first, then load paywall and products
        Botsi.activate(
            context = context,
            apiKey = app.botsiStorage.appKey,
            clearCache = clearCache,
            successCallback = {
                Botsi.getPaywall(
                    placementId = app.botsiStorage.placementId,
                    successCallback = { paywall ->
                        Botsi.getPaywallProducts(
                            paywall = paywall,
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
            },
            errorCallback = { e ->
                isError = true
                isLoading = false
                context.toast(e.message.orEmpty())
            }
        )
    }

    // Dark theme colors
    val backgroundDark = Color(0xFF2B3544)
    val containerDark = Color(0xFF3D4A5C)
    val accentBlue = Color(0xFF0EA5E9)
    val textPrimary = Color.White
    val textSecondary = Color(0xFF9CA3AF)
    val borderColor = Color(0xFF4B5563)

    Scaffold(
        containerColor = backgroundDark,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundDark,
                    titleContentColor = textPrimary
                ),
                title = {
                    Text(
                        text = "Botsi Classic",
                        style = TextStyle(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = accentBlue
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "Premium plan",
                    color = textPrimary,
                    fontSize = 24.sp,
                    style = TextStyle(fontWeight = FontWeight.SemiBold)
                )

                Text(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 48.dp),
                    text = "Subscribe to have full access to all premium features",
                    color = textSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 48.dp),
                    text = "Restore",
                    color = accentBlue,
                    fontSize = 16.sp,
                    style = TextStyle(fontWeight = FontWeight.Medium)
                )

                Spacer(Modifier.height(24.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .size(30.dp)
                            .align(Alignment.CenterHorizontally),
                        color = accentBlue
                    )
                } else {
                    if (!isSuccessPayment) {
                        products.forEach { product ->
                            SubscriptionItem(
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 24.dp),
                                title = product.name,
                                description = product.title,
                                price = product.subscriptionOffer?.pricingPhases?.firstOrNull()?.formattedPrice
                                    ?: "Free",
                                textColor = textPrimary,
                                textSecondaryColor = textSecondary,
                                accentColor = accentBlue,
                                borderColor = borderColor,
                                selected = selectedSub?.basePlanId == product.basePlanId,
                                onClick = {
                                    selectedSub =
                                        if (selectedSub?.basePlanId != product.basePlanId ||
                                            selectedSub?.subscriptionOffer?.offerId != product.subscriptionOffer?.offerId
                                        ) {
                                            product
                                        } else null
                                },
                            )
                        }
                    } else {
                        if (isError) {
                            Text(text = "Error occurred", color = textPrimary)
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 24.dp,
                                        end = 24.dp,
                                        top = 18.dp
                                    ),
                                    text = "Subscription success",
                                    fontSize = 18.sp,
                                    style = TextStyle(fontWeight = FontWeight.SemiBold),
                                    color = textPrimary
                                )
                                selectedSub?.let { sub ->
                                    SubscriptionItem2(
                                        modifier = Modifier.padding(
                                            vertical = 6.dp,
                                            horizontal = 48.dp
                                        ),
                                        title = sub.name,
                                        description = sub.name,
                                        textColor = textPrimary,
                                        textSecondaryColor = textSecondary,
                                        borderColor = borderColor,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isSuccessPayment) {
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                        enabled = selectedSub != null,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentBlue,
                            disabledContainerColor = accentBlue.copy(alpha = 0.5f)
                        ),
                        onClick = {
                            selectedSub?.let { sub ->
                                isLoading = true
                                Botsi.makePurchase(
                                    activity = context.findActivity(),
                                    product = sub,
                                    callback = { _, _ ->
                                        isSuccessPayment = true
                                        isLoading = false
                                        scope.launch { snackbarHostState.showSnackbar("Success") }
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
                            style = TextStyle(fontWeight = FontWeight.SemiBold),
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
private fun SubscriptionItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    price: String,
    textColor: Color,
    textSecondaryColor: Color,
    accentColor: Color,
    borderColor: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Spacer(Modifier.height(2.dp))
                Text(text = description, fontSize = 12.sp, color = textSecondaryColor)
            }
            Text(text = price, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = accentColor,
                    unselectedColor = borderColor
                )
            )
        }
    }
}

@Composable
private fun SubscriptionItem2(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    textColor: Color,
    textSecondaryColor: Color,
    borderColor: Color,
) {
    Column(
        modifier = modifier
            .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = textColor)
        Spacer(Modifier.height(8.dp))
        Text(text = description, fontSize = 14.sp, color = textSecondaryColor)
    }
}

private data class UiState(
    val paywall: com.botsi.domain.model.BotsiPaywall? = null,
    val products: List<BotsiProduct>? = null,
)

private fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is android.content.ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    throw IllegalStateException("Activity not found from context")
}

private fun Context.toast(message: String) {
    runBlocking<Any>(Dispatchers.Main) {
        Toast.makeText(this@toast, message, Toast.LENGTH_SHORT).show()
    }
}

private fun navigateOnUi(navController: NavHostController, route: String, clearCache: Boolean = false) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        val target = when (route) {
            "ui" -> "ui/${clearCache}"
            "classic" -> "classic/${clearCache}"
            else -> route
        }
        navController.navigate(target)
    } else {
        Handler(Looper.getMainLooper()).post {
            val target = when (route) {
                "ui" -> "ui/${clearCache}"
                "classic" -> "classic/${clearCache}"
                else -> route
            }
            navController.navigate(target)
        }
    }
}
