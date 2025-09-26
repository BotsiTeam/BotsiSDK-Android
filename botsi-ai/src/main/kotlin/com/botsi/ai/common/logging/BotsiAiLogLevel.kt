package com.botsi.ai.common.logging

import androidx.annotation.Keep
import androidx.annotation.RestrictTo
import java.util.logging.Level

/**
 * Log levels for the Botsi SDK.
 * These levels determine what messages are logged by the SDK.
 *
 * The log levels, in order of increasing verbosity, are:
 * - NONE: No logging
 * - ERROR: Only error messages
 * - WARN: Warning and error messages
 * - INFO: Informational, warning, and error messages (default)
 * - DEBUG: Debug, informational, warning, and error messages
 * - VERBOSE: All messages
 *
 * To configure the log level, use the [logLevel] parameter in [Botsi.activate] or call [Botsi.setLogLevel]
 * after activation.
 *
 * Example:
 * ```
 * // Configure log level during activation
 * Botsi.activate(
 *     context = applicationContext,
 *     apiKey = "your-api-key",
 *     logLevel = BotsiLogLevel.DEBUG
 * )
 *
 * // Or update log level after activation
 * Botsi.setLogLevel(BotsiLogLevel.VERBOSE)
 * ```
 */
@Keep
@RestrictTo(RestrictTo.Scope.LIBRARY)
enum class BotsiAiLogLevel(val value: Int) {
    /**
     * No logging.
     */
    NONE(0),

    /**
     * Error level logging only.
     * Logs only serious failures that prevent normal operation.
     */
    ERROR(1),

    /**
     * Warning level logging and above.
     * Logs potential problems that don't prevent normal operation, plus error messages.
     */
    WARN(2),

    /**
     * Info level logging and above.
     * Logs informational messages about normal operation, plus warnings and errors.
     */
    INFO(3),

    /**
     * Debug level logging and above.
     * Logs detailed information useful for debugging, plus info messages, warnings, and errors.
     */
    DEBUG(4),

    /**
     * Verbose level logging.
     * Logs all messages, including the most detailed information.
     */
    VERBOSE(5);

    /**
     * Convert to java.util.logging.Level
     */
    fun toJavaLevel(): Level {
        return when (this) {
            NONE -> Level.OFF
            ERROR -> Level.SEVERE
            WARN -> Level.WARNING
            INFO -> Level.INFO
            DEBUG -> Level.FINE
            VERBOSE -> Level.FINEST
        }
    }

    companion object {
        /**
         * Default log level for the SDK.
         */
        val DEFAULT = INFO
    }
}
