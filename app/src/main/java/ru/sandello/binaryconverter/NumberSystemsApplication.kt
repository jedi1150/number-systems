package ru.sandello.binaryconverter

import androidx.multidex.MultiDexApplication
import ru.sandello.binaryconverter.utils.Converter
import ru.sandello.binaryconverter.utils.Shared.converter

class NumberSystemsApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        converter = Converter()
    }
}