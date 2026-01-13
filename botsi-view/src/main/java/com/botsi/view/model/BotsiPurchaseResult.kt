package com.botsi.view.model

import com.botsi.domain.model.BotsiPurchase

/**
 * Represents the result of a purchase operation handled by the application.
 */
sealed class BotsiPurchaseResult {
    /**
     * Indicates a successful purchase operation.
     *
     * @property purchase The details of the completed purchase transaction.
     */
    class Success(val purchase: BotsiPurchase) : BotsiPurchaseResult()

    /**
     * Indicates that the purchase operation failed.
     *
     * @property exception The exception that caused the failure.
     */
    class Error(val exception: Throwable) : BotsiPurchaseResult()

    /**
     * Indicates that the application does not handle the purchase operation,
     * signaling the SDK to perform its default purchase flow.
     */
    internal object NotImplemented : BotsiPurchaseResult()
}