package com.botsi.logging

import androidx.annotation.RestrictTo

/**
 * Logging interface for the Botsi SDK.
 * This interface defines methods for logging messages at different levels.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface BotsiLogger {
    /**
     * Log a message at the ERROR level.
     * This level indicates a serious failure that prevents normal operation.
     *
     * @param message The message to log
     * @param throwable Optional throwable associated with the error
     */
    fun error(message: String, throwable: Throwable? = null)

    /**
     * Log a message at the WARN level.
     * This level indicates a potential problem that doesn't prevent normal operation.
     *
     * @param message The message to log
     * @param throwable Optional throwable associated with the warning
     */
    fun warn(message: String, throwable: Throwable? = null)

    /**
     * Log a message at the INFO level.
     * This level provides informational messages about normal operation.
     *
     * @param message The message to log
     */
    fun info(message: String)

    /**
     * Log a message at the DEBUG level.
     * This level provides detailed information useful for debugging.
     *
     * @param message The message to log
     */
    fun debug(message: String)

    /**
     * Log a message at the VERBOSE level.
     * This level provides the most detailed information.
     *
     * @param message The message to log
     */
    fun verbose(message: String)

    /**
     * Check if logging is enabled at the ERROR level.
     *
     * @return true if ERROR level logging is enabled
     */
    fun isErrorEnabled(): Boolean

    /**
     * Check if logging is enabled at the WARN level.
     *
     * @return true if WARN level logging is enabled
     */
    fun isWarnEnabled(): Boolean

    /**
     * Check if logging is enabled at the INFO level.
     *
     * @return true if INFO level logging is enabled
     */
    fun isInfoEnabled(): Boolean

    /**
     * Check if logging is enabled at the DEBUG level.
     *
     * @return true if DEBUG level logging is enabled
     */
    fun isDebugEnabled(): Boolean

    /**
     * Check if logging is enabled at the VERBOSE level.
     *
     * @return true if VERBOSE level logging is enabled
     */
    fun isVerboseEnabled(): Boolean
}