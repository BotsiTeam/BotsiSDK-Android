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
import com.botsi.domain.model.BotsiPurchase
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Manager for handling HTTP requests to the Botsi backend.
 *
 * This class provides a high-level API for interacting with Botsi services,
 * handling request creation via [BotsiRequestFactory] and execution via [BotsiHttpClient].
 * It also handles response parsing and error propagation.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiHttpManager(
    private val requestFactory: BotsiRequestFactory,
    private val httpClient: BotsiHttpClient,
) {

    /**
     * Creates a new user profile.
     */
    fun createProfile(
        profileId: String,
        customerUserId: String?,
        installationMeta: BotsiInstallationMetaDto,
    ): BotsiProfileDto {
        val request = requestFactory.createProfileRequest(
            profileId = profileId,
            customerUserId = customerUserId,
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

    /**
     * Retrieves an existing user profile.
     */
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

    /**
     * Updates user profile attributes.
     */
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

    /**
     * Retrieves the list of all product IDs available in the system.
     */
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

    /**
     * Retrieves paywall configuration for a placement.
     */
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

    /**
     * Retrieves UI configuration for a paywall.
     */
    fun getPaywallViewConfiguration(paywallId: Long): JsonElement {
        val response = httpClient.newRequest<JsonElement>(
            requestFactory.getViewConfigurationRequest(paywallId),
            getReflectType<JsonElement>(),
        )

        when (response) {
            is BotsiResponse.Success -> return response.body
            is BotsiResponse.Error -> throw response.error
        }
    }

    /**
     * Validates a purchase with the backend.
     */
    fun validatePurchase(
        profileId: String,
        purchase: BotsiPurchase,
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

    /**
     * Synchronizes purchase records with the backend.
     */
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