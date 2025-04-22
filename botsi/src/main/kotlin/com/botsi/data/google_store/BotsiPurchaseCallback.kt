package com.botsi.data.google_store

import com.android.billingclient.api.Purchase
import com.botsi.BotsiException

internal typealias BotsiPurchaseCallback = (purchase: Purchase?, error: BotsiException?) -> Unit