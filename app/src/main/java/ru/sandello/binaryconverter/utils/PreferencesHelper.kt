package ru.sandello.binaryconverter.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log


class PreferencesHelper(private val context: Context) {
    private var prefs: SharedPreferences? = null

    init {
        prefs = context.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE)
    }

    fun stringToPrefs(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }

    fun intToPrefs(key: String, value: Int) {
        prefs?.edit()?.putInt(key, value)?.apply()
    }

    fun longToPrefs(key: String, value: Long) {
        prefs?.edit()?.putLong(key, value)?.apply()
    }

    fun removeKeyFromPrefs(key: String) {
        prefs?.edit()?.remove(key)?.apply()
    }

    fun getStringFromPrefs(key: String): String? {
        return try {
            prefs?.getString(key, null)
        } catch (e: Exception) {
            return null
        }
    }

    fun getIntFromPrefs(key: String): Int {
        return try {
            prefs?.getInt(key, 0) ?: 0
        } catch (e: Exception) {
            return 0
        }
    }

    fun getLongFromPrefs(key: String): Long {
        return try {
            prefs?.getLong(key, 0) ?: 0
        } catch (e: Exception) {
            return 0
        }
    }
}