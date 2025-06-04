package com.competo.botsi.view.delegate

import androidx.annotation.RestrictTo
import com.botsi.Botsi
import com.competo.botsi.view.mapper.BotsiLayoutContentMapper
import com.competo.botsi.view.model.ui.BotsiPaywallUiAction
import com.competo.botsi.view.model.ui.BotsiPaywallUiSideEffect
import com.competo.botsi.view.model.ui.BotsiPaywallUiState
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
    private val contentMapper: BotsiLayoutContentMapper,
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
                    placementId = action.placementId,
                    paywallId = action.paywallId,
                    successCallback = {
                        coroutineScope.launch {
                            val content = contentMapper.map(it)
                            _uiState.update { BotsiPaywallUiState.Success(content) }
                        }
                    },
                    errorCallback = { error ->
                        error.message?.let { message ->
                            _uiState.update { BotsiPaywallUiState.Error(message) }
                            _uiSideEffect.tryEmit(BotsiPaywallUiSideEffect.Error(message))
                        }
                    }
                )
            }

            is BotsiPaywallUiAction.Dispose -> {
                coroutineScope.cancel()
            }
        }
    }
}