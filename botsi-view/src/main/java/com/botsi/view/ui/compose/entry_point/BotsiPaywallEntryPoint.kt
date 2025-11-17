package com.botsi.view.ui.compose.entry_point

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import com.botsi.Botsi
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.BotsiViewConfig
import com.botsi.view.delegate.BotsiPaywallDelegate
import com.botsi.view.di.BotsiPaywallDIManager
import com.botsi.view.handler.BotsiActionHandler
import com.botsi.view.handler.BotsiPublicEventHandler
import com.botsi.view.isNotEmpty
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.timer.BotsiTimerManager
import com.botsi.view.timer.BotsiTimerResolver
import com.botsi.view.ui.compose.composable.BotsiPaywallScreenComposable
import com.botsi.view.utils.findActivity
import kotlinx.coroutines.launch

/**
 * Main entry point composable for displaying Botsi paywalls in Jetpack Compose applications.
 * 
 * This composable provides a complete paywall UI solution that handles:
 * - Paywall display with customizable layouts
 * - Purchase flows integration with Google Play Billing
 * - User authentication and profile management
 * - Purchase restoration functionality
 * - Custom action handling and timer functionality
 * - Error handling and user feedback
 * 
 * The composable automatically manages its lifecycle, handles UI state, and provides
 * callbacks for all user interactions through the [BotsiPublicEventHandler] interface.
 * 
 * **Important**: Ensure the Botsi SDK is activated with [com.botsi.Botsi.activate] before
 * using this composable. The paywall data should be loaded using [com.botsi.Botsi.getPaywall]
 * and [com.botsi.Botsi.getPaywallProducts] before passing to this composable.
 * 
 * @param viewConfig Configuration containing the paywall and products to display.
 *                   Should include both paywall configuration from [com.botsi.Botsi.getPaywall]
 *                   and products with pricing from [com.botsi.Botsi.getPaywallProducts].
 *                   If empty or null, the composable will not display any content.
 * 
 * @param timerResolver Optional custom timer resolver for countdown functionality.
 *                      Defaults to [BotsiTimerResolver.default] which provides a 1-hour
 *                      countdown from the current time. Implement custom logic to provide
 *                      different timer behaviors for different timer IDs.
 * 
 * @param eventHandler Optional event handler for responding to user interactions.
 *                     Implement this interface to handle login actions, custom actions,
 *                     purchase success/failure, restore success/failure, and timer expiration.
 *                     If null, default behaviors will be used (e.g., back navigation for close).
 * 
 * @sample
 * ```kotlin
 * @Composable
 * fun MyPaywallScreen() {
 *     var viewConfig by remember { mutableStateOf(BotsiViewConfig()) }
 *     
 *     LaunchedEffect(Unit) {
 *         Botsi.getPaywall(
 *             placementId = "premium_upgrade",
 *             successCallback = { paywall ->
 *                 Botsi.getPaywallProducts(paywall) { products ->
 *                     viewConfig = BotsiViewConfig(paywall, products)
 *                 }
 *             }
 *         )
 *     }
 *     
 *     BotsiPaywallEntryPoint(
 *         viewConfig = viewConfig,
 *         eventHandler = object : BotsiPublicEventHandler {
 *             override fun onSuccessPurchase(profile: BotsiProfile, purchase: BotsiPurchase) {
 *                 // Handle successful purchase
 *                 navigateToMainScreen()
 *             }
 *             
 *             override fun onLoginAction() {
 *                 // Handle login button tap
 *                 navigateToLogin()
 *             }
 *             
 *             // Implement other required methods...
 *         }
 *     )
 * }
 * ```
 * 
 * @see BotsiViewConfig
 * @see BotsiPublicEventHandler
 * @see BotsiTimerResolver
 * @see com.botsi.Botsi.getPaywall
 * @see com.botsi.Botsi.getPaywallProducts
 * @see com.botsi.Botsi.logShowPaywall
 * 
 * @since 1.0.0
 */
@OptIn(UnstableApi::class)
@Composable
fun BotsiPaywallEntryPoint(
    viewConfig: BotsiViewConfig,
    timerResolver: BotsiTimerResolver = BotsiTimerResolver.default,
    eventHandler: BotsiPublicEventHandler? = null,
) {
    val context = LocalContext.current

    // Default action handler that implements all paywall interactions
    // This handler bridges between the internal paywall UI and the public event handler
    val defaultClickHandler = object : BotsiActionHandler {

        // Handle close button clicks by triggering back navigation
        override fun onCloseClick() {
            context.findActivity()?.onBackPressed()
        }

        // Delegate login clicks to the public event handler
        override fun onLoginClick() {
            eventHandler?.onLoginAction()
        }

        // Handle restore purchases by calling the Botsi SDK and forwarding results
        override fun onRestoreClick() {
            Botsi.restorePurchases(
                successCallback = { eventHandler?.onSuccessRestore(it) },
                errorCallback = { eventHandler?.onErrorRestore(it) }
            )
        }

        // Handle external link clicks by opening them in the default browser
        override fun onLinkClick(url: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (e: Exception) {
                // Show user-friendly error if link cannot be opened
                Toast.makeText(context, "Failed to open link", Toast.LENGTH_SHORT).show()
            }
        }

        // Delegate custom action clicks to the public event handler
        override fun onCustomActionClick(actionId: String) {
            eventHandler?.onCustomAction(actionId)
        }

        // Handle timer expiration events by notifying the public event handler
        override fun onTimerEnd(customActionId: String) {
            eventHandler?.onTimerEnd(customActionId)
        }

        // Handle purchase clicks by initiating the purchase flow through Botsi SDK
        override fun onPurchaseClick(product: BotsiProduct) {
            context.findActivity()?.let {
                Botsi.makePurchase(
                    activity = it,
                    product = product,
                    callback = { profile, purchase ->
                        eventHandler?.onSuccessPurchase(
                            profile,
                            purchase
                        )
                    },
                    errorCallback = { error ->
                        eventHandler?.onErrorPurchase(error)
                    }
                )
            }
        }
    }

    // Set up dependency injection manager with all required dependencies
    // This manager provides all the necessary components for the paywall functionality
    val diManager =
        remember(viewConfig, eventHandler) {
            BotsiPaywallDIManager(
                context = context,
                timerResolver = timerResolver,
                clickHandler = defaultClickHandler,
            )
        }

    // Get the main paywall delegate that manages UI state and business logic
    val delegate = remember(viewConfig, eventHandler) { diManager.inject<BotsiPaywallDelegate>() }

    // Set up UI components for user feedback and coroutine management
    val snackbarHostState = remember(Unit) { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Observe UI state and side effects from the delegate
    val uiState by delegate.uiState.collectAsState()
    val uiSideEffect by delegate.uiSideEffect.collectAsState(BotsiPaywallUiSideEffect.None)

    // Initialize the paywall view when the composable is first created
    LaunchedEffect(Unit) {
        delegate.onAction(BotsiPaywallUiAction.View)
    }

    // Log paywall impression for analytics when paywall data is available
    LaunchedEffect(Unit) {
        viewConfig.paywall?.let {
            Botsi.logShowPaywall(it)
        }
    }

    // Get timer manager for handling countdown timers in the paywall
    val timerManager = diManager.inject<BotsiTimerManager>()

    // Only render the paywall UI if we have valid configuration data
    if (viewConfig.isNotEmpty()) {
        // Load paywall data into the delegate when configuration changes
        LaunchedEffect(viewConfig) {
            viewConfig.paywall?.let {
                delegate.onAction(
                    BotsiPaywallUiAction.Load(
                        paywall = it,
                        products = viewConfig.products.orEmpty()
                    )
                )
            }
        }

        // Clean up timer resources when the composable is disposed
        DisposableEffect(Unit) {
            onDispose {
                timerManager.dispose(scope)
            }
        }

        // Render the main paywall UI composable
        BotsiPaywallScreenComposable(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            timerManager = timerManager,
            scope = scope,
            onAction = delegate::onAction
        )
    }

    // Handle UI side effects (errors, notifications, etc.) regardless of paywall state
    BotsiCollectUiSideEffect(
        uiSideEffect = uiSideEffect,
        snackbarHostState = snackbarHostState,
        onAction = delegate::onAction,
    )
}

/**
 * Internal composable that handles UI side effects from the paywall delegate.
 * 
 * This composable observes UI side effects (such as error messages) and presents them
 * to the user through appropriate UI components like snackbars. It acts as a bridge
 * between the business logic layer and the UI presentation layer.
 * 
 * @param uiSideEffect The current UI side effect to handle. Can be None, Error, etc.
 * @param snackbarHostState The snackbar host state for displaying error messages.
 * @param onAction Callback to send actions back to the paywall delegate when side effects are handled.
 */
@Composable
private fun BotsiCollectUiSideEffect(
    uiSideEffect: BotsiPaywallUiSideEffect,
    snackbarHostState: SnackbarHostState,
    onAction: (BotsiPaywallUiAction) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (uiSideEffect) {
        is BotsiPaywallUiSideEffect.None -> {}
        is BotsiPaywallUiSideEffect.Error -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    val result = snackbarHostState.showSnackbar(uiSideEffect.message)
                    if (result == SnackbarResult.Dismissed) {
                        onAction(BotsiPaywallUiAction.None)
                    }
                }
            }
        }
    }
}
