package com.botsi.domain.interactor.purchase

import android.app.Activity
import androidx.annotation.RestrictTo
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.botsi.domain.model.BotsiProduct
import com.botsi.domain.model.BotsiProfile
import com.botsi.domain.model.BotsiSubscriptionUpdateParameters
import kotlinx.coroutines.flow.Flow

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal interface BotsiPurchaseInteractor {

    fun makePurchase(
        activity: Activity,
        product: BotsiProduct,
        offer: ProductDetails.SubscriptionOfferDetails?,
        subscriptionUpdateParams: BotsiSubscriptionUpdateParameters?,
        isOfferPersonalized: Boolean,
    ): Flow<Pair<BotsiProfile, Purchase?>?>

    fun syncPurchases(): Flow<BotsiProfile>

}