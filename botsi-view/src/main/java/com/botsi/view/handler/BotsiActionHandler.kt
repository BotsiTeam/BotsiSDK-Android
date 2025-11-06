package com.botsi.view.handler

import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase

/**
 * Interface for handling click actions in Botsi paywall components.
 * This handler provides callbacks for various button actions that can occur
 * within the paywall UI.
 */
internal interface BotsiActionHandler {
    fun onCloseClick()
    fun onLoginClick()
    fun onRestoreClick()
    fun onLinkClick(url: String)
    fun onPurchaseClick(product: BotsiProduct)
    fun onCustomActionClick(actionId: String)

    fun onTimerEnd(customActionId: String)
}
