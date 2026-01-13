package com.botsi.data.repository

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.data.model.dto.BotsiPaywallDto
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiPurchaseRecordDto
import com.botsi.data.model.dto.BotsiSyncPurchaseDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.domain.model.BotsiPurchase
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing Botsi data.
 *
 * This repository coordinates data access between the network (via [com.botsi.data.http.BotsiHttpManager])
 * and local storage (via [com.botsi.data.storage.BotsiStorageManager]). It provides a unified API
 * for managing user profiles, validating purchases, and retrieving paywall configurations.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal interface BotsiRepository {

    /**
     * Flow emitting the current user profile state.
     */
    val profileStateFlow: Flow<BotsiProfileDto>

    /**
     * Retrieves the existing profile or creates a new one for the given user ID.
     */
    fun getOrCreateProfile(customerUserId: String?): Flow<BotsiProfileDto>

    /**
     * Updates user profile attributes.
     */
    fun updateProfile(customerUserId: String?,params: BotsiUpdateProfileParametersDto?): Flow<BotsiProfileDto>

    /**
     * Validates a purchase with the Botsi backend.
     *
     * @param purchase The purchase details from Google Play.
     * @param productDetails The product details associated with the purchase.
     */
    fun validatePurchase(
        purchase: BotsiPurchase,
        productDetails: BotsiPurchasableProductDto,
    ): Flow<BotsiProfileDto>

    /**
     * Retrieves the list of all product IDs available in the system.
     */
    fun getProductIds(): Flow<List<String>>

    /**
     * Retrieves paywall configuration for a specific placement.
     */
    fun getPaywall(placementId: String): Flow<BotsiPaywallDto>

    /**
     * Retrieves UI configuration for a specific paywall.
     */
    fun getPaywallViewConfiguration(paywallId: Long): Flow<JsonElement>

    /**
     * Synchronizes local purchase history with the backend.
     */
    fun syncPurchases(details: List<Pair<BotsiPurchaseRecordDto, ProductDetails>>): Flow<BotsiProfileDto>

    /**
     * Saves synchronized purchases to local storage.
     */
    fun saveSyncedPurchases(purchases: List<BotsiSyncPurchaseDto>)

    /**
     * Retrieves synchronized purchases from local storage.
     */
    fun getSyncedPurchases(): List<BotsiSyncPurchaseDto>

    /**
     * Clears all locally cached data.
     */
    fun clearCache()

}