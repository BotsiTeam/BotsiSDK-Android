package com.botsi.view.model

/**
 * Represents the result of a restore operation handled by the application.
 */
sealed class BotsiRestoreResult {
    /**
     * Indicates a successful restore operation.
     *
     */
    class Success() : BotsiRestoreResult()

    /**
     * Indicates that the restore operation failed.
     *
     * @property exception The exception that caused the failure.
     */
    class Error(val exception: Throwable) : BotsiRestoreResult()

    /**
     * Indicates that the application does not handle the restore operation,
     * signaling the SDK to perform its default restore flow.
     */
    internal object NotImplemented : BotsiRestoreResult()
}