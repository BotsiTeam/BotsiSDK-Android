package com.botsi.data.factory

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiRestoreProductAttributesDto
import com.botsi.data.model.dto.BotsiRestoreProductDetailsDto
import com.botsi.data.model.dto.BotsiRestoreProductDto
import com.botsi.data.model.dto.BotsiRestoreProductInfoRequest
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.data.model.request.BotsiCreateProfileRequest
import com.botsi.data.model.request.BotsiUpdateProfileRequest
import com.botsi.data.model.request.BotsiValidateReceiptRequest
import com.botsi.data.model.request.BotsiValidateReceiptRequest.BotsiOneTimePurchaseOfferDetailsRequestData
import com.botsi.data.model.request.BotsiValidateReceiptRequest.BotsiPricingPhaseRequestData
import com.botsi.data.model.request.BotsiValidateReceiptRequest.BotsiSubscriptionOfferDetailsRequestData

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiRequestDataFactory {

    fun createValidateReceiptRequest(
        token: String?,
        profileId: String,
        product: BotsiPurchasableProductDto,
    ): BotsiValidateReceiptRequest {
        return BotsiValidateReceiptRequest(
            profileId = profileId,
            token = token,
            productId = product.productId,
            placementId = product.placementId,
            paywallId = product.paywallId,
            abTestId = product.abTestId,
            isSubscription = product.currentSubOfferDetails != null,
            oneTimePurchaseOfferDetails = product
                .currentOneTmeOfferDetails
                ?.let { oneTime ->
                    BotsiOneTimePurchaseOfferDetailsRequestData(
                        oneTime.priceAmountMicros,
                        oneTime.priceCurrencyCode,
                    )
                },
            subscriptionOfferDetails = product.currentSubOfferDetails?.let { sub ->
                BotsiSubscriptionOfferDetailsRequestData(
                    basePlanId = sub.basePlanId,
                    offerId = sub.offerId,
                    pricingPhases = sub.pricingPhases.map { pricingPhase ->
                        BotsiPricingPhaseRequestData(
                            pricingPhase.priceAmountMicros,
                            pricingPhase.priceCurrencyCode,
                            pricingPhase.billingPeriod,
                            pricingPhase.recurrenceMode.toLong(),
                            pricingPhase.billingCycleCount.toLong(),
                        )
                    }
                )
            }
        )
    }

    fun createCreateProfileRequest(
        customerUserId: String?,
        meta: BotsiInstallationMetaDto?
    ) = BotsiCreateProfileRequest(
        meta?.copy(customerUserId = customerUserId)
    )

    fun createUpdateProfileRequest(
        customerUserId: String?,
        params: BotsiUpdateProfileParametersDto?
    ) = BotsiUpdateProfileRequest(
        birthday = params?.birthday,
        email = params?.email,
        userName = params?.userName,
        gender = params?.gender,
        phone = params?.phone,
        custom = params?.custom,
        customerUserId = customerUserId
    )

    fun createSyncProductInfoRequest(
        profileId: String,
        purchases: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>
    ) = BotsiRestoreProductInfoRequest(
        attributes = BotsiRestoreProductAttributesDto(
            profileId = profileId,
            items = purchases.map {
                BotsiRestoreProductDto(
                    productId = it.second.productId,
                    purchaseToken = it.first.purchaseToken,
                    productDetails = BotsiRestoreProductDetailsDto(
                        oneTimePurchaseOfferDetails =
                            it.second.oneTimePurchaseOfferDetails?.let { purchase ->
                                BotsiOneTimePurchaseOfferDetailsRequestData(
                                    purchase.priceAmountMicros,
                                    purchase.priceCurrencyCode,
                                )
                            },
                        subscriptionOfferDetails = it.second.subscriptionOfferDetails
                            ?.map { sub ->
                                BotsiSubscriptionOfferDetailsRequestData(
                                    basePlanId = sub.basePlanId,
                                    offerId = sub.offerId,
                                    pricingPhases = sub.pricingPhases.pricingPhaseList.map { pricingPhase ->
                                        BotsiPricingPhaseRequestData(
                                            pricingPhase.priceAmountMicros,
                                            pricingPhase.priceCurrencyCode,
                                            pricingPhase.billingPeriod,
                                            pricingPhase.recurrenceMode.toLong(),
                                            pricingPhase.billingCycleCount.toLong(),
                                        )
                                    }
                                )
                            }
                    )
                )
            }
        )
    )
}