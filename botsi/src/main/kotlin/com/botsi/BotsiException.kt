package com.botsi

import androidx.annotation.Keep

/**
 * Exception thrown by the Botsi SDK when operations fail.
 *
 * This exception is used to wrap and provide additional context for errors that occur
 * during SDK operations such as network requests, billing operations, or data processing.
 * It includes an error code for programmatic error handling and detailed error messages
 * for debugging purposes.
 *
 * ## Common Error Scenarios
 * - Network connectivity issues when communicating with Botsi servers
 * - Invalid API keys or authentication failures
 * - Google Play Billing errors during purchase operations
 * - Invalid parameters passed to SDK methods
 * - Server-side errors or service unavailability
 *
 * ## Usage Example
 * ```kotlin
 * Botsi.getProfile(
 *     successCallback = { profile ->
 *         // Handle successful profile retrieval
 *     },
 *     errorCallback = { error ->
 *         when (error) {
 *             is BotsiException -> {
 *                 Log.e("Botsi", "SDK Error [${error.code}]: ${error.message}", error)
 *                 // Handle specific error codes if needed
 *                 when (error.code) {
 *                     401 -> handleAuthenticationError()
 *                     404 -> handleNotFoundError()
 *                     else -> handleGenericError(error)
 *                 }
 *             }
 *             else -> {
 *                 Log.e("Botsi", "Unexpected error: ${error.message}", error)
 *             }
 *         }
 *     }
 * )
 * ```
 *
 * @param message A human-readable description of the error
 * @param cause The underlying cause of this exception, if any
 * @param code An error code for programmatic error handling (-1 if not specified)
 * @since 1.0.0
 */
@Keep
class BotsiException(
    override val message: String?,
    override val cause: Throwable? = null,
    val code: Int = -1
) : Exception(message, cause)
