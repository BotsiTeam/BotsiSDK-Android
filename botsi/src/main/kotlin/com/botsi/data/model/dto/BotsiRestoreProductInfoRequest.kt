package com.botsi.data.model.dto

import androidx.annotation.RestrictTo
import com.botsi.data.model.request.BotsiValidateReceiptRequest
import com.botsi.data.model.request.BotsiValidateReceiptRequest.BotsiOneTimePurchaseOfferDetailsRequestData

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiRestoreProductInfoRequest(
    val attributes: BotsiRestoreProductAttributesDto
)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiRestoreProductAttributesDto(
    val profileId: String,
    val items: List<BotsiRestoreProductDto>,
)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiRestoreProductDto(
    val productId: String,
    val purchaseToken: String,
    val productDetails: BotsiRestoreProductDetailsDto,
)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiRestoreProductDetailsDto(
    val oneTimePurchaseOfferDetails: BotsiOneTimePurchaseOfferDetailsRequestData?,
    val subscriptionOfferDetails: List<BotsiValidateReceiptRequest.BotsiSubscriptionOfferDetailsRequestData>?,
)