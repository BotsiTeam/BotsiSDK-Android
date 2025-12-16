package com.botsi.example

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.botsi.Botsi
import com.botsi.example.storage.BotsiConfigsStorage

class BotsiApp : MultiDexApplication() {

    private var storage: BotsiConfigsStorage? = null

    val botsiStorage: BotsiConfigsStorage
        get() = storage ?: BotsiConfigsStorage(this).apply {
            storage = this
        }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }

}