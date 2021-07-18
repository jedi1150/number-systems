package ru.sandello.binaryconverter.utils

import android.content.Context

class ResourcesHelper(private val context: Context) {
    fun getString(resId: Int): String {
        return context.getString(resId)
    }

    fun getString(resId: Int, value: String?): String {
        return context.getString(resId, value)
    }

}