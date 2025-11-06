package com.botsi.view.handler

/**
 * Enum representing the different types of actions that can be triggered
 * in the Botsi paywall UI components.
 */
enum class BotsiActionType {
    /**
     * No action - button does nothing
     */
    None,
    
    /**
     * Close action - typically dismisses the paywall
     */
    Close,
    
    /**
     * Login action - navigates to login screen
     */
    Login,
    
    /**
     * Restore action - restores previous purchases
     */
    Restore,
    
    /**
     * Custom action - performs a custom action based on actionId
     */
    Custom,
    
    /**
     * Link action - opens a URL
     */
    Link,

    Purchase,
}