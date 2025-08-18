package com.botsi.domain.mapper

import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.botsi.data.model.dto.BotsiProfileDto
import com.botsi.data.model.dto.BotsiPurchasableProductDto
import com.botsi.data.model.dto.BotsiSubscriptionUpdateParametersDto
import com.botsi.data.model.dto.BotsiUpdateProfileParametersDto
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiPurchasableProduct
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import com.botsi.domain.model.BotsiUpdateProfileParameters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProfileDto.toDomain(): BotsiProfile {
    return BotsiProfile(
        profileId = profileId.orEmpty(),
        customerUserId = customerUserId.orEmpty(),
        accessLevels = accessLevels?.mapValues { it.value.toDomain() }.orEmpty(),
        subscriptions = subscriptions?.mapValues { it.value.toDomain() }.orEmpty(),
        nonSubscriptions = nonSubscriptions?.mapValues { it.value.toDomain() }.orEmpty(),
        custom = custom?.map { it.toDomain() }.orEmpty()
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProfileDto.AccessLevelDto.toDomain(): BotsiProfile.AccessLevel {
    return BotsiProfile.AccessLevel(
        createdDate = createdDate.toDate(),
        id = id ?: 0,
        isActive = isActive == true,
        sourceProductId = sourceProductId.orEmpty(),
        sourceBasePlanId = sourceBasePlanId.orEmpty(),
        store = store.orEmpty(),
        activatedAt = activatedAt.toDate(),
        isLifetime = isLifetime == true,
        isRefund = isRefund == true,
        willRenew = willRenew == true,
        isInGracePeriod = isInGracePeriod == true,
        cancellationReason = cancellationReason.orEmpty(),
        offerId = offerId.orEmpty(),
        startsAt = startsAt.toDate(),
        renewedAt = renewedAt.toDate(),
        expiresAt = expiresAt.toDate(),
        activeIntroductoryOfferType = activeIntroductoryOfferType.orEmpty(),
        activePromotionalOfferType = activePromotionalOfferType.orEmpty(),
        activePromotionalOfferId = activePromotionalOfferId.orEmpty(),
        unsubscribedAt = unsubscribedAt.toDate(),
        billingIssueDetectedAt = billingIssueDetectedAt.toDate(),
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProfileDto.SubscriptionDto.toDomain(): BotsiProfile.Subscription {
    return BotsiProfile.Subscription(
        createdDate = createdDate.toDate(),
        id = id ?: 0,
        isActive = isActive == true,
        sourceProductId = sourceProductId.orEmpty(),
        sourceBasePlanId = sourceBasePlanId.orEmpty(),
        store = store.orEmpty(),
        activatedAt = activatedAt.toDate(),
        isLifetime = isLifetime == true,
        isRefund = isRefund == true,
        willRenew = willRenew == true,
        isInGracePeriod = isInGracePeriod == true,
        cancellationReason = cancellationReason.orEmpty(),
        offerId = offerId.orEmpty(),
        startsAt = startsAt.toDate(),
        renewedAt = renewedAt.toDate(),
        expiresAt = expiresAt.toDate(),
        activeIntroductoryOfferType = activeIntroductoryOfferType.orEmpty(),
        activePromotionalOfferType = activePromotionalOfferType.orEmpty(),
        activePromotionalOfferId = activePromotionalOfferId.orEmpty(),
        unsubscribedAt = unsubscribedAt.toDate(),
        billingIssueDetectedAt = billingIssueDetectedAt.toDate(),
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProfileDto.NonSubscriptionDto.toDomain(): BotsiProfile.NonSubscription {
    return BotsiProfile.NonSubscription(
        isConsumable = isConsumable == true,
        isOneTime = isOneTime == true,
        isRefund = isRefund == true,
        purchasedAt = purchasedAt.toDate(),
        purchasedId = purchasedId.orEmpty(),
        store = store.orEmpty(),
        sourceProductId = sourceProductId.orEmpty(),
        transactionId = transactionId.orEmpty(),
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProfileDto.CustomEntryDto.toDomain(): BotsiProfile.CustomEntry {
    return BotsiProfile.CustomEntry(
        key = key.orEmpty(),
        value = value.orEmpty(),
        id = id.orEmpty(),
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiSubscriptionUpdateParameters.toDto(): BotsiSubscriptionUpdateParametersDto {
    return BotsiSubscriptionUpdateParametersDto(
        oldSubVendorProductId = oldSubVendorProductId,
        isOfferPersonalized = isOfferPersonalized,
        obfuscatedAccountId = obfuscatedAccountId,
        obfuscatedProfileId = obfuscatedProfileId,
        replacementMode = replacementMode
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiProduct.toPurchasableProduct(
    productDetails: ProductDetails,
): BotsiPurchasableProduct {
    return BotsiPurchasableProduct(
        productId = productId,
        type = type,
        placementId = placementId,
        abTestId = abTestId,
        paywallId = paywallId,
        isConsumable = isConsumable,
        currentSubOfferDetails = subscriptionOffer,
        currentOneTmeOfferDetails = onTimePurchaseOffers,
        productDetails = productDetails,
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiPurchasableProduct.toDto(): BotsiPurchasableProductDto {
    return BotsiPurchasableProductDto(
        productId = productId,
        type = type,
        currentSubOfferDetails = currentSubOfferDetails,
        currentOneTmeOfferDetails = currentOneTmeOfferDetails,
        productDetails = productDetails,
        isConsumable = isConsumable,
        placementId = placementId,
        paywallId = paywallId,
        abTestId = abTestId,
    )
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal fun BotsiUpdateProfileParameters.toDto(): BotsiUpdateProfileParametersDto {
    return BotsiUpdateProfileParametersDto(
        birthday = birthday,
        email = email,
        userName = userName,
        gender = gender?.name?.replaceFirstChar { it.lowercase() },
        phone = phone,
        custom = custom.map {
            BotsiProfile.CustomEntry(
                key = it.key,
                value = it.value,
                id = it.id,
            )
        },
    )
}

internal fun String?.toDate(): Date {
    return runCatching {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        val normalizedDateString = this?.replace("T", " ")?.replace("Z", "").orEmpty()
        dateFormatter.parse(normalizedDateString)
    }.getOrDefault(Date())
}