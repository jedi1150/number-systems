package ru.sandello.binaryconverter.utils

import android.annotation.SuppressLint

object Shared {
    var FRACTION_COUNT = 12

    @SuppressLint("StaticFieldLeak")
    lateinit var resourcesHelper: ResourcesHelper
    @SuppressLint("StaticFieldLeak")
    lateinit var preferencesHelper: PreferencesHelper

    lateinit var converter: Converter
}