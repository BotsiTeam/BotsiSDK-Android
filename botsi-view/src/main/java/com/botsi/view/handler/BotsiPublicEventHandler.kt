package com.botsi.view.handler

import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase

interface BotsiPublicEventHandler {

    fun onLoginAction()
    fun onCustomAction(actionId: String)

    fun onSuccessRestore(profile: BotsiProfile)
    fun onErrorRestore(error: Throwable)

    fun onSuccessPurchase(
        profile: BotsiProfile,
        purchase: BotsiPurchase,
    )

    fun onErrorPurchase(error: Throwable)

    fun onTimerEnd(actionId: String)
}
