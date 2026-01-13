package com.botsi.view.handler

import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.view.model.BotsiPurchaseResult
import com.botsi.view.model.BotsiRestoreResult

/**
 * Interface for handling public events from the Botsi SDK.
 *
 * This interface defines callback methods that are triggered by various user interactions
 * and system events within the Botsi paywall system. Implement this interface to handle
 * user actions, purchase flows, restore operations, and timer events.
 *
 * @since 1.0.0
 */
interface BotsiPublicEventHandler {

    /**
     * Called when the user triggers a login action within the Botsi paywall.
     *
     * This method is invoked when the user interacts with login-related UI elements
     * or when the system determines that user authentication is required.
     */
    fun onLoginAction(){}

    /**
     * Called when the user triggers a custom action within the Botsi paywall.
     *
     * Custom actions are user-defined interactions that can be configured within
     * the paywall system to trigger specific behaviors in the host application.
     *
     * @param actionId The unique identifier of the custom action that was triggered
     */
    fun onCustomAction(actionId: String){}

    /**
     * Called when a purchase restore operation completes successfully.
     *
     * This method is invoked after the SDK successfully restores the user's
     * previous purchases and updates their profile accordingly.
     *
     * @param profile The updated user profile containing restored purchase information
     */
    fun onSuccessRestore(profile: BotsiProfile){}

    /**
     * Called when a purchase restore operation fails.
     *
     * This method is invoked when the SDK encounters an error while attempting
     * to restore the user's previous purchases.
     *
     * @param error The throwable containing details about the restore failure
     */
    fun onErrorRestore(error: Throwable){}

    /**
     * Called when a purchase operation completes successfully.
     *
     * This method is invoked after the user successfully completes a purchase
     * transaction and the SDK has processed the purchase information.
     *
     * @param profile The updated user profile reflecting the new purchase
     * @param purchase The details of the completed purchase transaction
     */
    fun onSuccessPurchase(
        profile: BotsiProfile,
        purchase: BotsiPurchase,
    ){}

    /**
     * Called when a purchase operation fails.
     *
     * This method is invoked when the SDK encounters an error during the
     * purchase process, such as payment failures, network issues, or
     * validation errors.
     *
     * @param error The throwable containing details about the purchase failure
     */
    fun onErrorPurchase(error: Throwable){}

    /**
     * Called when a timer associated with a specific action reaches its end.
     *
     * This method is invoked when countdown timers or time-limited offers
     * within the paywall expire, allowing the host application to respond
     * to time-based events.
     *
     * @param actionId The unique identifier of the action whose timer has ended
     */
    fun onTimerEnd(actionId: String){}

    /**
     * Called when the SDK needs additional parameters for a subscription update.
     *
     * This method is invoked during the purchase process for a subscription that
     * replaces an existing one, if the SDK requires details such as the old
     * purchase token or the proration mode.
     *
     * @param product The product being purchased
     * @return The parameters for the subscription update, or null if not applicable
     */
    fun onAwaitSubscriptionsParams(product: BotsiProduct): BotsiSubscriptionUpdateParameters? {
        return null
    }

    /**
     * Called when a purchase is being processed by the application.
     *
     * Implement this method if you want to handle the purchase process manually.
     * Return [BotsiPurchaseResult.NotImplemented] to let the SDK handle the purchase.
     *
     * @param product The product being purchased
     * @return The result of the purchase processing
     */
    suspend fun onPurchaseProcessed(product: BotsiProduct): BotsiPurchaseResult {
        return BotsiPurchaseResult.NotImplemented
    }

    /**
     * Called when the restore action is triggered by the user.
     *
     * Implement this method if you want to handle the restore process manually.
     * Return [BotsiRestoreResult.NotImplemented] to let the SDK handle the restore.
     *
     * @return The result of the restore processing
     */
    suspend fun onRestoreClick(): BotsiRestoreResult {
        return BotsiRestoreResult.NotImplemented
    }
}
