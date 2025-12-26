package com.botsi.view.handler

import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchase
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters

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
    fun onLoginAction()

    /**
     * Called when the user triggers a custom action within the Botsi paywall.
     * 
     * Custom actions are user-defined interactions that can be configured within
     * the paywall system to trigger specific behaviors in the host application.
     * 
     * @param actionId The unique identifier of the custom action that was triggered
     */
    fun onCustomAction(actionId: String)

    /**
     * Called when a purchase restore operation completes successfully.
     * 
     * This method is invoked after the SDK successfully restores the user's
     * previous purchases and updates their profile accordingly.
     * 
     * @param profile The updated user profile containing restored purchase information
     */
    fun onSuccessRestore(profile: BotsiProfile)

    /**
     * Called when a purchase restore operation fails.
     * 
     * This method is invoked when the SDK encounters an error while attempting
     * to restore the user's previous purchases.
     * 
     * @param error The throwable containing details about the restore failure
     */
    fun onErrorRestore(error: Throwable)

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
    )

    /**
     * Called when a purchase operation fails.
     * 
     * This method is invoked when the SDK encounters an error during the
     * purchase process, such as payment failures, network issues, or
     * validation errors.
     * 
     * @param error The throwable containing details about the purchase failure
     */
    fun onErrorPurchase(error: Throwable)

    /**
     * Called when a timer associated with a specific action reaches its end.
     * 
     * This method is invoked when countdown timers or time-limited offers
     * within the paywall expire, allowing the host application to respond
     * to time-based events.
     * 
     * @param actionId The unique identifier of the action whose timer has ended
     */
    fun onTimerEnd(actionId: String)

    fun onAwaitSubscriptionsParams(product: BotsiProduct): BotsiSubscriptionUpdateParameters?
}
