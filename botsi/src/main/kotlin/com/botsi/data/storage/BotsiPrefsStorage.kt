package com.botsi.data.storage

import android.content.Context
import androidx.annotation.RestrictTo
import com.google.gson.Gson
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
internal class BotsiPrefsStorage(
    context: Context,
    private val gson: Gson
) {

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    @JvmSynthetic
    fun clearData(keys: Set<String>) {
        prefs.edit(commit = true) {
            keys.forEach(::remove)
        }
    }

    @JvmSynthetic
    fun clearData() {
        prefs.edit(commit = true) {
            clear()
        }
    }

    @JvmSynthetic
    fun getKeysToRemove(containsKeys: Set<String>, startsWithKeys: Set<String>): Set<String> =
        prefs.all.keys.filterTo(mutableSetOf()) { key ->
            key != null && (key in containsKeys || startsWithKeys.firstOrNull { key.startsWith(it) } != null)
        }

    @JvmSynthetic
    fun getBoolean(key: String, defaultValue: Boolean?) =
        if (prefs.contains(key)) {
            prefs.getBoolean(key, defaultValue ?: false)
        } else {
            defaultValue
        }

    @JvmSynthetic
    fun saveBoolean(key: String, value: Boolean) =
        prefs.edit(commit = true) {
            putBoolean(key, value)
        }

    @JvmSynthetic
    fun getLong(key: String, defaultValue: Long?) =
        if (prefs.contains(key)) {
            prefs.getLong(key, defaultValue ?: 0L)
        } else {
            defaultValue
        }

    @JvmSynthetic
    fun saveLong(key: String, value: Long) =
        prefs.edit(commit = true) {
            putLong(key, value)
        }

    @JvmSynthetic
    fun getString(key: String) = prefs.getString(key, null)

    @JvmSynthetic
    fun saveString(key: String, value: String) =
        prefs.edit(commit = true) {
            putString(key, value)
        }

    @JvmSynthetic
    fun saveStrings(map: Map<String, String>) =
        prefs.edit(commit = true) {
            map.forEach { (key, value) -> putString(key, value) }
        }

    @JvmSynthetic
    inline fun <reified T> getData(key: String, classOfT: Class<T>? = null): T? {
        return prefs.getString(key, null)?.takeIf(::isNotEmpty)?.let {
            try {
                classOfT?.let { classOfT ->
                    gson.fromJson(it, classOfT)
                } ?: gson.fromJson<T>(it, object : TypeToken<T>() {}.type)
            } catch (e: Exception) {
                null
            }
        }
    }

    @JvmSynthetic
    fun saveData(key: String, data: Any?) {
        prefs.edit(commit = true) { putString(key, gson.toJson(data)) }
    }

    @JvmSynthetic
    fun contains(key: String) = prefs.contains(key)

    private fun isNotEmpty(str: String) = str.length > 4

    private companion object {
        const val PREF_NAME = "BotsiPrefs"
    }
}