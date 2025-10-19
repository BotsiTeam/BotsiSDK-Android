package com.botsi.view.handler

import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase

/**
 * Public interface for handling click actions in Botsi paywall components.
 * This handler provides callbacks for various button actions that can occur
 * within the paywall UI, using type-safe enums that can be accessed from external modules.
 */
interface BotsiPublicEventHandler {

    fun onLoginAction()
    fun onCloseAction()
    fun onRestoreAction()
    fun onCustomAction(actionId: String, actionLabel: String? = null)
    fun onSuccessPurchase(
        profile: BotsiProfile,
        purchase: BotsiPurchase,
    )

    fun onErrorPurchase(error: Throwable)
}
