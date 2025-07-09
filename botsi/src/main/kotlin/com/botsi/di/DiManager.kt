package com.botsi.di

import android.content.Context
import androidx.annotation.RestrictTo
import com.botsi.analytic.AnalyticsManager
import com.botsi.analytic.AnalyticsTracker
import com.botsi.data.factory.BotsiRequestDataFactory
import com.botsi.data.factory.BotsiRequestFactory
import com.botsi.data.google_store.BotsiGoogleStoreManager
import com.botsi.data.http.BotsiHttpManager
import com.botsi.data.http.BotsiResponseInterpolator
import com.botsi.data.http.client.BotsiHttpClient
import com.botsi.data.http.client.BotsiHttpClientImpl
import com.botsi.data.repository.BotsiRepository
import com.botsi.data.repository.BotsiRepositoryImpl
import com.botsi.data.service.BotsiInstallationMetaRetrieverService
import com.botsi.data.service.retriever.BotsiAdIdRetriever
import com.botsi.data.service.retriever.BotsiAppSetIdRetriever
import com.botsi.data.service.retriever.BotsiNetworkIpRetriever
import com.botsi.data.service.retriever.BotsiStoreCountryRetriever
import com.botsi.data.service.retriever.BotsiUserAgentRetriever
import com.botsi.data.storage.BotsiPrefsStorage
import com.botsi.data.storage.BotsiStorageManager
import com.botsi.domain.interactor.products.BotsiProductsInteractor
import com.botsi.domain.interactor.products.BotsiProductsInteractorImpl
import com.botsi.domain.interactor.profile.BotsiProfileInteractor
import com.botsi.domain.interactor.profile.BotsiProfileInteractorImpl
import com.botsi.domain.interactor.purchase.BotsiPurchaseInteractor
import com.botsi.domain.interactor.purchase.BotsiPurchaseInteractorImpl
import com.botsi.logging.BotsiLogLevel
import com.botsi.logging.BotsiLogger
import com.botsi.logging.BotsiLoggerImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class DiManager {

    private val dependencies = mutableMapOf<Class<*>, Any>()

    fun initDi(
        context: Context,
        apiKey: String,
        logLevel: BotsiLogLevel = BotsiLogLevel.DEFAULT,
    ) {
        // Initialize logger first so it's available for other components
        dependencies[BotsiLogger::class.java] = BotsiLoggerImpl(logLevel)

        dependencies[Gson::class.java] = GsonBuilder().create()
        dependencies[BotsiPrefsStorage::class.java] = BotsiPrefsStorage(context, inject())
        dependencies[BotsiStorageManager::class.java] = BotsiStorageManager(inject())

        dependencies[BotsiResponseInterpolator::class.java] = BotsiResponseInterpolator(inject(), inject())
        dependencies[BotsiHttpClient::class.java] = BotsiHttpClientImpl(inject())
        dependencies[BotsiRequestDataFactory::class.java] = BotsiRequestDataFactory()
        dependencies[BotsiRequestFactory::class.java] = BotsiRequestFactory(inject(), inject(), apiKey)
        dependencies[BotsiHttpManager::class.java] = BotsiHttpManager(inject(), inject())

        dependencies[BotsiGoogleStoreManager::class.java] = BotsiGoogleStoreManager(context, inject())
        dependencies[BotsiAppSetIdRetriever::class.java] = BotsiAppSetIdRetriever()
        dependencies[BotsiNetworkIpRetriever::class.java] = BotsiNetworkIpRetriever()
        dependencies[BotsiAdIdRetriever::class.java] = BotsiAdIdRetriever()
        dependencies[BotsiUserAgentRetriever::class.java] = BotsiUserAgentRetriever()
        dependencies[BotsiStoreCountryRetriever::class.java] = BotsiStoreCountryRetriever(inject())

        dependencies[BotsiInstallationMetaRetrieverService::class.java] = BotsiInstallationMetaRetrieverService(
            context,
            inject(),
            inject(),
            inject(),
            inject(),
            inject(),
        )

        dependencies[AnalyticsTracker::class.java] = AnalyticsManager(inject(), inject(), inject(), inject())

        dependencies[BotsiRepository::class.java] = BotsiRepositoryImpl(inject(), inject(), inject())
        dependencies[BotsiProfileInteractor::class.java] = BotsiProfileInteractorImpl(inject())
        dependencies[BotsiProductsInteractor::class.java] = BotsiProductsInteractorImpl(inject(), inject())
        dependencies[BotsiPurchaseInteractor::class.java] = BotsiPurchaseInteractorImpl(inject(), inject(), inject())
    }

    /**
     * Updates the log level used by the SDK.
     *
     * @param logLevel The new log level
     */
    fun updateLogLevel(logLevel: BotsiLogLevel) {
        (dependencies[BotsiLogger::class.java] as? BotsiLoggerImpl)?.updateLogLevel(logLevel)
    }

    inline fun <reified T> inject(): T {
        return dependencies[T::class.java] as T
    }

}
