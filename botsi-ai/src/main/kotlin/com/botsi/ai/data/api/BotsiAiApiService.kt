package com.botsi.ai.data.api

import com.botsi.ai.data.model.BotsiAiGetPaywallRequestDto
import com.botsi.ai.data.model.BotsiAiInstallationMetaDto
import com.botsi.ai.data.model.BotsiAiPaywallDto
import com.botsi.ai.data.model.BotsiAiProfileDto
import com.botsi.ai.data.model.BotsiAiResponse
import com.botsi.ai.data.model.BotsiAiValidatePurchaseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface BotsiAiApiService {

    @POST("v1/web-api/profiles")
    suspend fun createProfile(
        @Header("Authorization") secretKey: String,
        @Body meta: BotsiAiInstallationMetaDto
    ): BotsiAiResponse<BotsiAiProfileDto>

    @POST("v1/web-api/paywalls")
    suspend fun getPaywall(
        @Header("Authorization") secretKey: String,
        @Body body: BotsiAiGetPaywallRequestDto,
    ): BotsiAiResponse<BotsiAiPaywallDto>

    @POST("v1/web-api/purchases/play-store/validate")
    suspend fun validatePayment(
        @Header("Authorization") secretKey: String,
        @Body body: BotsiAiValidatePurchaseDto
    ): BotsiAiResponse<BotsiAiProfileDto>

}