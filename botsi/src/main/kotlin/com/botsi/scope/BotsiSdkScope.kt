package com.botsi.scope

import androidx.annotation.RestrictTo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal val botsiSdkScope =
    CoroutineScope(
        SupervisorJob() + Dispatchers.Default + CoroutineExceptionHandler { ctx, exc ->
            exc.printStackTrace()
        },
    )

@JvmSynthetic
internal fun launch(action: suspend () -> Unit) {
    botsiSdkScope.launch { action() }
}

@JvmSynthetic
internal fun <T> Flow<T>.flowOnMain(): Flow<T> =
    this.flowOn(Dispatchers.Main)