package com.botsi.data.http

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.data.factory.BotsiRequestFactory
import com.botsi.data.http.client.BotsiHttpClient
import com.botsi.data.model.dto.BotsiInstallationMetaDto
import com.botsi.data.model.dto.BotsiPaywallDto
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.data.model.response.BotsiBaseResponse
import com.botsi.data.model.response.BotsiResponse
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiHttpManager(
    private val requestFactory: BotsiRequestFactory,
    private val httpClient: BotsiHttpClient,
) {

    fun createProfile(
        profileId: String,
        installationMeta: BotsiInstallationMetaDto,
    ): BotsiProfileDto {
        val request = requestFactory.createProfileRequest(
            profileId = profileId,
            installationMeta = installationMeta
        )
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiProfileDto>>(
            request,
            getReflectType<BotsiBaseResponse<BotsiProfileDto>>()
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun getProfile(profileId: String): BotsiProfileDto {
        val request = requestFactory.getProfileRequest(profileId = profileId)
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiProfileDto>>(
            request,
            getReflectType<BotsiBaseResponse<BotsiProfileDto>>()
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun updateProfile(
        profileId: String,
        customerUserId: String?,
        params: BotsiUpdateProfileParametersDto?,
    ): BotsiProfileDto {
        val request = requestFactory.updateProfileRequest(
            profileId = profileId,
            params = params,
            customerUserId = customerUserId
        )
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiProfileDto>>(
            request,
            getReflectType<BotsiBaseResponse<BotsiProfileDto>>()
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun getProductIds(): List<String> {
        val response = httpClient.newRequest<BotsiBaseResponse<List<String>>>(
            requestFactory.getProductIdsRequest(),
            getReflectType<BotsiBaseResponse<List<String>>>(),
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun getPaywall(
        placementId: String,
        profileId: String,
    ): BotsiPaywallDto {
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiPaywallDto>>(
            requestFactory.getPaywallRequest(placementId, profileId),
            getReflectType<BotsiBaseResponse<BotsiPaywallDto>>(),
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun validatePurchase(
        profileId: String,
        purchase: Purchase,
        product: BotsiPurchasableProductDto,
    ): BotsiProfileDto {
        val request = requestFactory.validatePurchaseRequest(
            profileId = profileId,
            purchase = purchase,
            product = product
        )
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiProfileDto>>(
            request,
            getReflectType<BotsiBaseResponse<BotsiProfileDto>>()
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    fun syncPurchases(
        profileId: String,
        purchases: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>
    ): BotsiProfileDto {
        val request = requestFactory.syncPurchasesRequest(
            profileId = profileId,
            purchases = purchases,
        )
        val response = httpClient.newRequest<BotsiBaseResponse<BotsiProfileDto>>(
            request,
            getReflectType<BotsiBaseResponse<BotsiProfileDto>>()
        )
        when (response) {
            is BotsiResponse.Success -> return response.body.data
            is BotsiResponse.Error -> throw response.error
        }
    }

    private inline fun <reified T> getReflectType(): Type {
        return object : TypeToken<T>() {}.type
    }

}