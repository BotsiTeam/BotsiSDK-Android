package com.botsi.ai.ui.model

import android.app.Activity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
private fun randomId(): String = Uuid.random().toHexString()

sealed interface BotsiAiUiAction {
    data class Init(
        val placementId: String,
        val secretKey: String,
    ) : BotsiAiUiAction

    data class Pay(val activity: Activity) : BotsiAiUiAction
    data class Restore(val activity: Activity) : BotsiAiUiAction
    data object Back : BotsiAiUiAction
    data object View : BotsiAiUiAction
    data class SelectProduct(val product: BotsiAiProductUi) : BotsiAiUiAction
}

sealed interface BotsiAiUiSideEffect {
    data class None(val id: String = randomId()) : BotsiAiUiSideEffect
    data class Back(val id: String = randomId()) : BotsiAiUiSideEffect
}

data class BotsiAiUiState(
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
    val isSuccess: Boolean = false,
    val placementId: String = "",
    val secretKey: String = "",
    val paywallType: BotsiUiPaywallType = BotsiUiPaywallType.None,
    val paywall: BotsiAiPaywallUi = BotsiAiPaywallUi(),
    val selectedProduct: BotsiAiProductUi? = null,
)