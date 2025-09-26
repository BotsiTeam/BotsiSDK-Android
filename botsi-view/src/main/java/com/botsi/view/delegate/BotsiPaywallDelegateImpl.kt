package com.botsi.view.delegate

import androidx.annotation.RestrictTo
import com.botsi.Botsi
import com.botsi.view.handler.BotsiActionType
import com.botsi.view.handler.BotsiClickHandler
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
    private val clickHandler: BotsiClickHandler? = null,
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
                            _uiSideEffect.tryEmit(BotsiPaywallUiSideEffect.Error(it))
                        }
                    }
                )
            }

            is BotsiPaywallUiAction.Load -> {
                _uiState.update { BotsiPaywallUiState.Loading }

                Botsi.getPaywallViewConfiguration(
                    paywallId = action.paywallId,
//                    paywallId = 571,
                    successCallback = {
                        coroutineScope.launch {
                            val content = paywallBlocksMapper.map(it)
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

            is BotsiPaywallUiAction.ButtonClick -> {
                val actionType = when (action.action) {
                    is BotsiButtonAction.None -> BotsiActionType.None
                    is BotsiButtonAction.Close -> BotsiActionType.Close
                    is BotsiButtonAction.Login -> BotsiActionType.Login
                    is BotsiButtonAction.Restore -> BotsiActionType.Restore
                    is BotsiButtonAction.Custom -> BotsiActionType.Custom
                    is BotsiButtonAction.Link -> BotsiActionType.Link
                }
                clickHandler?.onButtonClick(
                    actionType = actionType,
                    actionId = action.actionId,
                    url = (action.action as? BotsiButtonAction.Link)?.url
                )
            }

            is BotsiPaywallUiAction.TopButtonClick -> {
                val actionType = when (action.topButton.action) {
                    is BotsiButtonAction.None -> BotsiActionType.None
                    is BotsiButtonAction.Close -> BotsiActionType.Close
                    is BotsiButtonAction.Login -> BotsiActionType.Login
                    is BotsiButtonAction.Restore -> BotsiActionType.Restore
                    is BotsiButtonAction.Custom -> BotsiActionType.Custom
                    is BotsiButtonAction.Link -> BotsiActionType.Link
                    null -> BotsiActionType.None
                }
                clickHandler?.onTopButtonClick(actionType, action.topButton.actionId)
            }

            is BotsiPaywallUiAction.LinkClick -> {
                clickHandler?.onLinkClick(action.url)
            }

            is BotsiPaywallUiAction.CustomAction -> {
                clickHandler?.onCustomAction(action.actionId, action.actionLabel)
            }
        }
    }
}
