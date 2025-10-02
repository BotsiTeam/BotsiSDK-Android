package com.botsi.ai.ui.delegate

import android.widget.Toast
import com.botsi.ai.domain.interactor.BotsiAiInteractor
import com.botsi.ai.ui.mapper.toDomain
import com.botsi.ai.ui.mapper.toPaywallType
import com.botsi.ai.ui.mapper.toUi
import com.botsi.ai.ui.model.BotsiAiUiAction
import com.botsi.ai.ui.model.BotsiAiUiSideEffect
import com.botsi.ai.ui.model.BotsiAiUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface BotsiAiDelegate {
    val state: StateFlow<BotsiAiUiState>
    val uiSideEffect: Flow<BotsiAiUiSideEffect>

    fun onAction(action: BotsiAiUiAction)
}

class BotsiAiDelegateImpl(
    private val interactor: BotsiAiInteractor
) : BotsiAiDelegate {

    private val scope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { ctx, exc ->
                exc.printStackTrace()
            },
        )

    private val _state = MutableStateFlow(BotsiAiUiState())
    override val state: StateFlow<BotsiAiUiState> = _state

    private val _uiSideEffect = MutableSharedFlow<BotsiAiUiSideEffect>(extraBufferCapacity = 1)
    override val uiSideEffect: Flow<BotsiAiUiSideEffect> = _uiSideEffect

    override fun onAction(action: BotsiAiUiAction) {
        when (action) {
            is BotsiAiUiAction.Init -> {
                scope.launch {
                    try {
                        _state.update {
                            it.copy(
                                isLoading = true,
                                placementId = action.placementId,
                                secretKey = action.secretKey
                            )
                        }
                        interactor.createProfileIfNecessary(action.secretKey)
                        val paywall = interactor.getPaywall(
                            placementId = action.placementId,
                            secretKey = action.secretKey,
                        )
                        val paywallUi = paywall.toUi()
                        _state.update {
                            it.copy(
                                paywallType = paywall.externalId.toPaywallType(),
                                paywall = paywallUi,
                                selectedProduct = paywallUi.sourceProducts.firstOrNull()
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        _state.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                }
            }

            is BotsiAiUiAction.Pay -> {
                state.value.selectedProduct?.let { product ->
                    scope.launch {
                        try {
                            _state.update { it.copy(isLoadingButton = true) }
                            val result = interactor.makePurchase(
                                secretKey = state.value.secretKey,
                                placementId = state.value.placementId,
                                activity = action.activity,
                                paywall = state.value.paywall.toDomain(),
                                product = product.toDomain(),
                            )
                            _state.update { it.copy(isSuccess = result) }
                        } catch (t: Throwable) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    action.activity,
                                    t.message.orEmpty(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            _state.update { it.copy(isSuccess = false) }
                        } finally {
                            _state.update { it.copy(isLoadingButton = false) }
                        }
                    }
                }
            }

            is BotsiAiUiAction.Restore -> {
                scope.launch {
                    try {
                        _state.update { it.copy(isLoadingButton = true) }
                        interactor.restorePurchases(state.value.secretKey)
                    } catch (t: Throwable) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                action.activity,
                                t.message.orEmpty(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } finally {
                        _state.update { it.copy(isLoadingButton = false) }
                    }
                }
            }

            is BotsiAiUiAction.View -> {
                scope.launch {
                    try {
                        interactor.logPaywallShown(
                            secretKey = state.value.secretKey,
                            paywallId = state.value.paywall.id,
                            placementId = state.value.placementId,
                            isExperiment = state.value.paywall.isExperiment,
                            aiPricingModelId = state.value.paywall.aiPricingModelId
                        )
                    } catch (t: Throwable) {
                        t.printStackTrace()
                    }
                }
            }

            is BotsiAiUiAction.SelectProduct -> {
                _state.update { it.copy(selectedProduct = action.product) }
            }

            is BotsiAiUiAction.Back -> _uiSideEffect.tryEmit(BotsiAiUiSideEffect.Back())
        }
    }


}