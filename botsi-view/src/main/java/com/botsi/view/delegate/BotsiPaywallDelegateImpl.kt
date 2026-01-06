package com.botsi.view.delegate

import androidx.annotation.RestrictTo
import com.botsi.Botsi
import com.botsi.domain.model.BotsiProduct
import com.botsi.view.handler.BotsiActionHandler
import com.botsi.view.mapper.BotsiPaywallBlocksMapper
import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.ui.BotsiPaywallUiAction
import com.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.botsi.view.model.ui.BotsiPaywallUiState
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

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPaywallDelegateImpl(
    private val paywallBlocksMapper: BotsiPaywallBlocksMapper,
    private val eventHandler: BotsiActionHandler? = null,
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
                eventHandler?.onTimerEnd(
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
            is BotsiButtonAction.Close -> eventHandler?.onCloseClick()
            is BotsiButtonAction.Login -> eventHandler?.onLoginClick()
            is BotsiButtonAction.Restore -> eventHandler?.onRestoreClick()
            is BotsiButtonAction.Custom -> eventHandler?.onCustomActionClick(action.id)
            is BotsiButtonAction.Link -> eventHandler?.onLinkClick(action.url)
            is BotsiButtonAction.Purchase -> selectedProduct?.let { eventHandler?.onPurchaseClick(it) }
            is BotsiButtonAction.None -> {}
        }
    }
}
