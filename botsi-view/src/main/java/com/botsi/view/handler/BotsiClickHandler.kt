package com.botsi.view.handler

import com.botsi.view.model.content.BotsiButtonAction
import com.botsi.view.model.content.BotsiTopButton

/**
 * Interface for handling click actions in Botsi paywall components.
 * This handler provides callbacks for various button actions that can occur
 * within the paywall UI.
 */
internal interface BotsiClickHandler {

    /**
     * Called when a button with a specific action is clicked.
     *
     * @param action The button action that was triggered
     * @param actionId Optional action identifier for custom actions
     */
    fun onButtonClick(
        actionType: BotsiActionType,
        actionId: String? = null,
        url: String? = null
    )

    /**
     * Called when a top button is clicked.
     *
     * @param topButton The top button that was clicked
     */
    fun onTopButtonClick(
        actionType: BotsiActionType,
        actionId: String? = null
    )

    /**
     * Called when a link action is triggered.
     *
     * @param url The URL to be opened
     */
    fun onLinkClick(url: String)

    /**
     * Called when a custom action is triggered.
     *
     * @param actionId The custom action identifier
     * @param actionLabel Optional action label
     */
    fun onCustomAction(
        actionId: String,
        actionLabel: String? = null
    )
}
