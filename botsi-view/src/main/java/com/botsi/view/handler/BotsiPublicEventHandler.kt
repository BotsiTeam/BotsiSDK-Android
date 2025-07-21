package com.botsi.view.handler

/**
 * Public interface for handling click actions in Botsi paywall components.
 * This handler provides callbacks for various button actions that can occur
 * within the paywall UI, using type-safe enums that can be accessed from external modules.
 */
interface BotsiPublicEventHandler {

    fun onLoginAction()
    fun onRestoreAction()
    fun onCustomAction(actionId: String, actionLabel: String? = null)
}
