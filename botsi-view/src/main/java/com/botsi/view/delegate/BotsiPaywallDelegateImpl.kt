package com.botsi.view.delegate

import android.content.Context
import androidx.annotation.RestrictTo
import com.botsi.Botsi
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.handler.BotsiActionHandler
import com.botsi.view.mapper.BotsiPaywallBlocksMapper
import com.botsi.view.model.BotsiPurchaseResult
import com.botsi.view.model.BotsiRestoreResult
import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.model.ui.BotsiPaywallUiState
import com.botsi.view.utils.findActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Default implementation of [BotsiPaywallDelegate].
 *
 * This class handles the business logic for the paywall UI, including loading configuration,
 * handling user actions (purchases, restores, etc.), and managing the UI state.
 *
 * @property paywallBlocksMapper Mapper for converting paywall configuration into UI blocks.
 * @property eventHandler Handler for actions triggered by the UI.
 * @property context Android context for launching activities and finding the host activity.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPaywallDelegateImpl(
    private val paywallBlocksMapper: BotsiPaywallBlocksMapper,
    private val eventHandler: BotsiActionHandler,
    private val context: Context,
) : BotsiPaywallDelegate {

    private lateinit var coroutineScope: CoroutineScope

    private val _uiState = MutableStateFlow<BotsiPaywallUiState>(BotsiPaywallUiState.None)
    override val uiState: StateFlow<BotsiPaywallUiState>
        get() = _uiState

    private val _uiSideEffect = MutableSharedFlow<BotsiPaywallUiSideEffect>(
        extraBufferCapacity = 1
    )
    override val uiSideEffect: Flow<BotsiPaywallUiSideEffect>
        get() = _uiSideEffect

    private val selectedProduct: BotsiProduct?
        get() = (uiState.value as BotsiPaywallUiState.Success).selectedProductId?.let {
            (uiState.value as? BotsiPaywallUiState.Success)
                ?.products?.find {
                    it.botsiProductId == (uiState.value as BotsiPaywallUiState.Success).selectedProductId
                }
        }

    override fun onAction(action: BotsiPaywallUiAction) {
        when (action) {
            is BotsiPaywallUiAction.None -> {
                _uiSideEffect.tryEmit(BotsiPaywallUiSideEffect.None)
            }

            is BotsiPaywallUiAction.View -> {
                coroutineScope = CoroutineScope(
                    SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { context, ex ->
                        ex.message?.let {
                            _uiSideEffect.tryEmit(BotsiPaywallUiSideEffect.Error(it))
                        }
                    }
                )
            }

            is BotsiPaywallUiAction.Load -> {
                _uiState.update { BotsiPaywallUiState.Loading }

                Botsi.getPaywallViewConfiguration(
                    paywallId = action.paywall.id,
                    successCallback = {
                        coroutineScope.launch {
                            val content = paywallBlocksMapper.map(it)
                            _uiState.update {
                                BotsiPaywallUiState.Success(
                                    content = content,
                                    paywall = action.paywall,
                                    products = action.products
                                )
                            }
                        }
                    },
                    errorCallback = { error ->
                        error.message?.let { message ->
                            _uiState.update { BotsiPaywallUiState.Error(message) }
                        }
                    }
                )
            }

            is BotsiPaywallUiAction.Dispose -> {
                coroutineScope.cancel()
            }

            is BotsiPaywallUiAction.ButtonClick -> {
                handleClick(action.action)
            }

            is BotsiPaywallUiAction.TimerEnd -> {
                eventHandler.onTimerEnd(
                    customActionId = action.customActionId
                )
            }

            is BotsiPaywallUiAction.ProductSelected -> {
                _uiState.update {
                    if (it is BotsiPaywallUiState.Success) {
                        it.copy(
                            selectedProductId = action.productId
                        )
                    } else {
                        it
                    }
                }
            }
        }
    }

    private fun handleClick(action: BotsiButtonAction) {
        when (action) {
            is BotsiButtonAction.Close -> eventHandler.onCloseClick()
            is BotsiButtonAction.Login -> eventHandler.onLoginClick()
            is BotsiButtonAction.Restore -> handleRestore()
            is BotsiButtonAction.Custom -> eventHandler.onCustomActionClick(action.id)
            is BotsiButtonAction.Link -> eventHandler.onLinkClick(action.url)
            is BotsiButtonAction.Purchase -> handlePurchase()

            is BotsiButtonAction.None -> {}
        }
    }

    private fun handleRestore() {
        coroutineScope.launch {
            when (val result = eventHandler.onRestoreClick()) {
                is BotsiRestoreResult.Success -> Botsi.getProfile(
                    successCallback = eventHandler::onSuccessRestore,
                    errorCallback = eventHandler::onErrorPurchase
                )

                is BotsiRestoreResult.Error -> eventHandler.onErrorRestore(result.exception)
                is BotsiRestoreResult.NotImplemented -> Botsi.restorePurchases(
                    successCallback = eventHandler::onSuccessRestore,
                    errorCallback = eventHandler::onErrorRestore
                )
            }
        }
    }

    private fun handlePurchase() {
        if (selectedProduct == null) return

        coroutineScope.launch {
            when (val result = eventHandler.onPurchaseProcessed(selectedProduct!!)) {
                is BotsiPurchaseResult.Success -> {
                    Botsi.getProfile(
                        successCallback = { profile ->
                            eventHandler.onSuccessPurchase(
                                profile,
                                result.purchase
                            )
                        },
                        errorCallback = eventHandler::onErrorPurchase
                    )
                }

                is BotsiPurchaseResult.NotImplemented -> {
                    withContext(Dispatchers.Main) {
                        context.findActivity()?.let {
                            Botsi.makePurchase(
                                activity = it,
                                product = selectedProduct!!,
                                subscriptionUpdateParams = eventHandler.onAwaitSubscriptionsParams(
                                    selectedProduct!!
                                ),
                                callback = eventHandler::onSuccessPurchase,
                                errorCallback = eventHandler::onErrorPurchase
                            )
                        }
                    }
                }

                is BotsiPurchaseResult.Error -> eventHandler.onErrorPurchase(result.exception)
            }
        }
    }

}
