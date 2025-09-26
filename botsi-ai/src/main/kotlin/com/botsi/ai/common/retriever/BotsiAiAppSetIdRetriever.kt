package com.botsi.ai.common.retriever

import android.content.Context
import androidx.annotation.RestrictTo
import com.google.android.gms.appset.AppSet
import com.google.android.gms.tasks.Tasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Semaphore

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
 class BotsiAiAppSetIdRetriever {

    private val semaphore = Semaphore(1)

    @Volatile
    private var cachedAppSetId: String? = null

    fun getAppSetIdIfAvailable(context: Context): Flow<String> =
        flow {
            cachedAppSetId?.let { appSetId ->
                emit(appSetId)
                return@flow
            }

            semaphore.acquire()
            cachedAppSetId?.let { appSetId ->
                semaphore.release()
                emit(appSetId)
                return@flow
            }

            val appSetId = try {
                Tasks.await(AppSet.getClient(context).appSetIdInfo).id
            } catch (e: Exception) {
                null
            }

            cachedAppSetId = appSetId
            semaphore.release()
            emit(appSetId.orEmpty())
        }
}