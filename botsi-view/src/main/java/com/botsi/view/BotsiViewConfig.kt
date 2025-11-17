package com.botsi.view

import com.botsi.domain.model.BotsiPaywall
import com.botsi.domain.model.BotsiProduct

/**
 * Configuration class for the Botsi view system.
 * 
 * This class encapsulates the configuration data required to display and operate
 * the Botsi paywall interface. It contains the paywall definition and associated
 * product information that will be presented to the user.
 * 
 * @property paywall The paywall configuration containing UI layout, content, and behavior settings.
 *                   Can be null if no paywall is configured or available.
 * @property products The list of products available for purchase within the paywall.
 *                    Can be null or empty if no products are configured or available.
 * 
 * @since 1.0.0
 */
class BotsiViewConfig(
    val paywall: BotsiPaywall? = null,
    val products: List<BotsiProduct>? = null,
)

/**
 * Extension function to check if the BotsiViewConfig contains valid configuration data.
 * 
 * This function determines whether the configuration is considered "not empty" by checking
 * if a paywall is present. The presence of a paywall indicates that the configuration
 * contains meaningful data that can be used to display the Botsi interface.
 * 
 * @return `true` if the configuration contains a non-null paywall, `false` otherwise.
 *         Note that this function only checks for paywall presence and does not validate
 *         the products list.
 * 
 * @since 1.0.0
 */
fun BotsiViewConfig.isNotEmpty(): Boolean = paywall != null
