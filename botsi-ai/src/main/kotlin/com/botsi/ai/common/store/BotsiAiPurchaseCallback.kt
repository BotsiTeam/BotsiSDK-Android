package com.botsi.ai.common.store

import com.android.billingclient.api.Purchase
import com.botsi.ai.common.error.BotsiAiException

internal typealias BotsiPurchaseCallback = (purchase: Purchase?, error: BotsiAiException?) -> Unit