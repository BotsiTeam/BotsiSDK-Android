package com.botsi.data.factory

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.analytic.AnalyticsEvent
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.data.model.request.BotsiRequest
import com.botsi.data.model.request.BotsiSendEventRequest
import com.google.gson.Gson

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiRequestFactory(
    private val requestDataFactory: BotsiRequestDataFactory,
    private val gson: Gson,
    private val apiKey: String,
) {

    private fun String.toProfileEndPoint(): String {
        return "profiles/$this/"
    }

    @JvmSynthetic
    fun getProfileRequest(profileId: String) = buildRequest {
        method = BotsiRequest.Method.GET
        endPoint = profileId.toProfileEndPoint()
    }

    @JvmSynthetic
    fun updateProfileRequest(
        profileId: String,
        customerUserId: String?,
        params: BotsiUpdateProfileParametersDto?,
    ) = buildRequest {
        method = BotsiRequest.Method.PATCH
        body = gson.toJson(
            requestDataFactory.createUpdateProfileRequest(
                customerUserId,
                params,
            )
        )
        endPoint = profileId.toProfileEndPoint()
    }


    @JvmSynthetic
    fun createProfileRequest(
        profileId: String,
        customerUserId: String?,
        installationMeta: BotsiInstallationMetaDto?,
    ) = buildRequest {
        method = BotsiRequest.Method.POST
        body = gson.toJson(
            requestDataFactory.createCreateProfileRequest(
                customerUserId = customerUserId,
                meta = installationMeta,
            )
        )
        endPoint = profileId.toProfileEndPoint()
    }

    @JvmSynthetic
    fun validatePurchaseRequest(
        profileId: String,
        purchase: Purchase,
        product: BotsiPurchasableProductDto,
    ) = buildRequest {
        method = BotsiRequest.Method.POST
        endPoint = "purchases/play-store/validate/"
        body = gson.toJson(
            requestDataFactory.createValidateReceiptRequest(
                token = purchase.purchaseToken,
                profileId = profileId,
                product = product
            )
        )
    }

    @JvmSynthetic
    fun syncPurchasesRequest(
        profileId: String,
        purchases: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>
    ) = buildRequest {
        method = BotsiRequest.Method.POST
        body = gson.toJson(
            requestDataFactory.createSyncProductInfoRequest(
                profileId = profileId,
                purchases = purchases
            )
        )
        endPoint = "purchases/play-store/restore/"
    }

    @JvmSynthetic
    fun getProductIdsRequest() = buildRequest {
        method = BotsiRequest.Method.GET
        endPoint = "products/products-ids/play_store"
    }

    @JvmSynthetic
    fun getPaywallRequest(
        id: String,
        profileId: String,
    ) = buildRequest {
        method = BotsiRequest.Method.GET
        endPoint = "paywalls/$id?profileId=$profileId&store=play_store"
    }

    @JvmSynthetic
    fun sendAnalyticsEventsRequest(events: List<AnalyticsEvent>) =
        buildRequest {
            method = BotsiRequest.Method.POST
            endPoint = "events"
            body = gson.toJson(BotsiSendEventRequest.create(events))
        }

    @JvmSynthetic
    fun getViewConfigurationRequest(paywallId: Long) = buildRequest {
        method = BotsiRequest.Method.GET
        endPoint = "paywalls/$paywallId/builder"
    }

    private inline fun buildRequest(action: BotsiRequest.Builder.() -> Unit) =
        BotsiRequest
            .Builder()
            .apply {
                action()
                if (method != BotsiRequest.Method.GET)
                    headers += listOf(BotsiRequest.Header("Content-type", "application/json"))
                addDefaultHeaders()
            }.build()

    private fun BotsiRequest.Builder.addDefaultHeaders() {
        val defaultHeaders = setOfNotNull(
            BotsiRequest.Header("Accept-Encoding", "gzip"),
            BotsiRequest.Header("Content-type", "application/json"),
            BotsiRequest.Header(AUTHORIZATION_KEY, apiKey),
        )
        headers += defaultHeaders
    }

    private companion object {
        private const val AUTHORIZATION_KEY = "Authorization"
    }

}