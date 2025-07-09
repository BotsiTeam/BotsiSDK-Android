package com.botsi.logging

import androidx.annotation.RestrictTo
import java.util.logging.Logger

/**
 * Implementation of [BotsiLogger] that uses Java's logging system.
 * This class respects the configured log level to determine what messages to log.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiLoggerImpl(
    private var logLevel: BotsiLogLevel = BotsiLogLevel.DEFAULT
) : BotsiLogger {

    private val logger = Logger.getLogger("Botsi")

    init {
        // Configure the logger with the initial log level
        updateLogLevel(logLevel)
    }

    /**
     * Update the log level used by this logger.
     *
     * @param level The new log level
     */
    fun updateLogLevel(level: BotsiLogLevel) {
        logLevel = level
        logger.level = level.toJavaLevel()
    }

    override fun error(message: String, throwable: Throwable?) {
        if (isErrorEnabled()) {
            if (throwable != null) {
                logger.severe("$message: ${throwable.message}")
                throwable.printStackTrace()
            } else {
                logger.severe(message)
            }
        }
    }

    override fun warn(message: String, throwable: Throwable?) {
        if (isWarnEnabled()) {
            if (throwable != null) {
                logger.warning("$message: ${throwable.message}")
                throwable.printStackTrace()
            } else {
                logger.warning(message)
            }
        }
    }

    override fun info(message: String) {
        if (isInfoEnabled()) {
            logger.info(message)
        }
    }

    override fun debug(message: String) {
        if (isDebugEnabled()) {
            logger.fine(message)
        }
    }

    override fun verbose(message: String) {
        if (isVerboseEnabled()) {
            logger.finest(message)
        }
    }

    override fun isErrorEnabled(): Boolean {
        return logLevel.value >= BotsiLogLevel.ERROR.value
    }

    override fun isWarnEnabled(): Boolean {
        return logLevel.value >= BotsiLogLevel.WARN.value
    }

    override fun isInfoEnabled(): Boolean {
        return logLevel.value >= BotsiLogLevel.INFO.value
    }

    override fun isDebugEnabled(): Boolean {
        return logLevel.value >= BotsiLogLevel.DEBUG.value
    }

    override fun isVerboseEnabled(): Boolean {
        return logLevel.value >= BotsiLogLevel.VERBOSE.value
    }
}