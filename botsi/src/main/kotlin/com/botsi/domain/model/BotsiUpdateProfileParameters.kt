package com.botsi.domain.model

import androidx.annotation.Keep

/**
 * Parameters for updating a user profile in the Botsi system.
 *
 * This class contains optional parameters that can be used to update various
 * attributes of a user profile. All parameters are optional, allowing for
 * partial updates. Use this class with [com.botsi.Botsi.updateProfile] to
 * modify user profile information.
 *
 * ## Usage Example
 * ```kotlin
 * val updateParams = BotsiUpdateProfileParameters(
 *     email = "user@example.com",
 *     userName = "john_doe",
 *     birthday = "1990-01-15",
 *     gender = "male",
 *     phone = "+1234567890",
 *     custom = listOf(
 *         BotsiUpdateProfileParameters.CustomAttributesEntry(
 *             key = "preferred_language",
 *             value = "en"
 *         ),
 *         BotsiUpdateProfileParameters.CustomAttributesEntry(
 *             key = "user_level",
 *             value = "premium"
 *         )
 *     )
 * )
 * 
 * Botsi.updateProfile(
 *     params = updateParams,
 *     errorCallback = { error ->
 *         Log.e("Botsi", "Failed to update profile", error)
 *     }
 * )
 * ```
 *
 * @param birthday The user's birthday in ISO 8601 format (YYYY-MM-DD)
 * @param email The user's email address
 * @param userName The user's username or display name
 * @param gender The user's gender (e.g., "male", "female", "other")
 * @param phone The user's phone number in international format
 * @param custom List of custom attributes to associate with the user profile
 * @since 1.0.0
 * @see com.botsi.Botsi.updateProfile
 * @see CustomAttributesEntry
 */
@Keep
data class BotsiUpdateProfileParameters(
    val birthday: String? = null,
    val email: String? = null,
    val userName: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val custom: List<CustomAttributesEntry> = emptyList()
) {

    /**
     * Represents a custom attribute entry for user profile updates.
     *
     * Custom attributes allow storing additional user-specific data that can be
     * retrieved later with the user profile. These are useful for storing
     * app-specific user preferences, metadata, or any other custom information.
     *
     * @param key The key/name of the custom attribute
     * @param value The value of the custom attribute
     * @param id Optional unique identifier for this custom entry
     * @since 1.0.0
     * @see BotsiUpdateProfileParameters
     */
    @Keep
    data class CustomAttributesEntry(
        val key: String? = null,
        val value: String? = null,
        val id: String? = null,
    )

}
