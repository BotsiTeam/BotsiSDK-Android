package com.botsi.ai.di

import android.content.Context
import com.botsi.ai.common.logging.BotsiAiLogger
import com.botsi.ai.common.logging.BotsiAiLoggerImpl
import com.botsi.ai.common.retriever.BotsiAiAdIdRetriever
import com.botsi.ai.common.retriever.BotsiAiAppSetIdRetriever
import com.botsi.ai.common.retriever.BotsiAiNetworkIpRetriever
import com.botsi.ai.common.retriever.BotsiAiStoreCountryRetriever
import com.botsi.ai.common.retriever.BotsiAiUserAgentRetriever
import com.botsi.ai.common.store.BotsiAiGoogleStoreManager
import com.botsi.ai.data.api.BotsiAiApiService
import com.botsi.ai.data.repository.BotsiAiRepository
import com.botsi.ai.data.repository.BotsiAiRepositoryImpl
import com.botsi.ai.data.service.BotsiAiInstallationMetaRetrieverService
import com.botsi.ai.data.storage.BotsiAiPrefsStorage
import com.botsi.ai.data.storage.BotsiAiStorageManager
import com.botsi.ai.domain.interactor.BotsiAiInteractor
import com.botsi.ai.domain.interactor.BotsiAiInteractorImpl
import com.botsi.ai.ui.delegate.BotsiAiDelegate
import com.botsi.ai.ui.delegate.BotsiAiDelegateImpl
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class BotsiAiDiManager(context: Context) {

    private val dependencies = mutableMapOf<Class<*>, Any>()

    init {
        dependencies[BotsiAiPrefsStorage::class.java] = BotsiAiPrefsStorage(context, Gson())
        dependencies[BotsiAiStorageManager::class.java] = BotsiAiStorageManager(inject())
        dependencies[BotsiAiLogger::class.java] = BotsiAiLoggerImpl()
        dependencies[Retrofit::class.java] = initRetrofit()
        dependencies[BotsiAiApiService::class.java] = inject<Retrofit>()
            .create(BotsiAiApiService::class.java)
        dependencies[BotsiAiGoogleStoreManager::class.java] =
            BotsiAiGoogleStoreManager(context, inject())
        dependencies[BotsiAiAppSetIdRetriever::class.java] = BotsiAiAppSetIdRetriever()
        dependencies[BotsiAiNetworkIpRetriever::class.java] = BotsiAiNetworkIpRetriever()
        dependencies[BotsiAiAdIdRetriever::class.java] = BotsiAiAdIdRetriever()
        dependencies[BotsiAiUserAgentRetriever::class.java] = BotsiAiUserAgentRetriever()
        dependencies[BotsiAiStoreCountryRetriever::class.java] =
            BotsiAiStoreCountryRetriever(inject())

        dependencies[BotsiAiInstallationMetaRetrieverService::class.java] =
            BotsiAiInstallationMetaRetrieverService(
                context,
                inject(),
                inject(),
                inject(),
                inject(),
                inject(),
            )
        dependencies[BotsiAiRepository::class.java] = BotsiAiRepositoryImpl(
            inject(),
            inject(),
            inject(),
            inject(),
        )
        dependencies[BotsiAiInteractor::class.java] = BotsiAiInteractorImpl(
            inject(),
        )
        dependencies[BotsiAiDelegate::class.java] = BotsiAiDelegateImpl(
            inject(),
        )
    }

    private fun initRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://swytapp-test.com.ua/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    inline fun <reified T> inject(): T {
        return dependencies[T::class.java] as T
    }
}