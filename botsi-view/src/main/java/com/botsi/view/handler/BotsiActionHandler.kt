package com.botsi.view.handler

import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.view.model.BotsiPurchaseResult
import com.botsi.view.model.BotsiRestoreResult

/**
 * Internal interface for handling actions within the Botsi paywall components.
 * This handler bridges the gap between the UI components and the business logic
 * in the paywall delegate.
 */
internal interface BotsiActionHandler {
    /**
     * Called when the close button is clicked in the paywall.
     */
    fun onCloseClick()

    /**
     * Called when the login button is clicked in the paywall.
     */
    fun onLoginClick()

    /**
     * Called when the restore button is clicked in the paywall.
     */
    suspend fun onRestoreClick(): BotsiRestoreResult

    /**
     * Called when a restore operation is successful.
     */
    fun onSuccessRestore(profile: BotsiProfile)

    /**
     * Called when a restore operation fails.
     */
    fun onErrorRestore(error: Throwable)

    /**
     * Called when a link (URL) is clicked in the paywall.
     */
    fun onLinkClick(url: String)

    /**
     * Called when a custom action button is clicked in the paywall.
     */
    fun onCustomActionClick(actionId: String)

    /**
     * Called to process a purchase for a specific product.
     */
    suspend fun onPurchaseProcessed(product: BotsiProduct): BotsiPurchaseResult

    /**
     * Called when a purchase operation is successful.
     */
    fun onSuccessPurchase(
        profile: BotsiProfile,
        purchase: BotsiPurchase,
    )

    /**
     * Called when a purchase operation fails.
     */
    fun onErrorPurchase(error: Throwable)

    /**
     * Called when the SDK needs additional parameters for a subscription update.
     */
    fun onAwaitSubscriptionsParams(product: BotsiProduct): BotsiSubscriptionUpdateParameters?

    /**
     * Called when a timer associated with a specific action reaches its end.
     */
    fun onTimerEnd(customActionId: String)
}
