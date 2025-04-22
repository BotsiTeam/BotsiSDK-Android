package com.botsi.example

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.botsi.Botsi

class BotsiApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        Botsi.activate(
            context = this,
            apiKey = "pk_knzfDupGEwEveheF.EEURFTlSDGB47hVva5zRV7Zmc",
        )
    }
}