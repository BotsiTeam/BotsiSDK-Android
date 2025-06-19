package com.botsi.data.http

import android.content.Context
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
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class BotsiHttpManager(
    private val context: Context,
    private val requestFactory: BotsiRequestFactory,
    private val httpClient: BotsiHttpClient,
) {

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

    private fun readFileFromAssets(filename: String): String {

        //We will build the string line by line from *.txt file
        val builder = StringBuilder()

        //BufferedReader is needed to read the *.txt file
        //Create and Initialize BufferedReader
        val reader = BufferedReader(InputStreamReader(context.assets.open(filename)))

        //This variable will contain the text
        var line: String?

        //check if there is a more line available
        while (reader.readLine().also { line = it } != null) {
            builder.append(line).append("\n")
        }

        //Need to close the BufferedReader
        reader.close()

        //just return the String of the *.txt file
        return builder.toString()
    }

    private inline fun <reified T> getReflectType(): Type {
        return object : TypeToken<T>() {}.type
    }

}