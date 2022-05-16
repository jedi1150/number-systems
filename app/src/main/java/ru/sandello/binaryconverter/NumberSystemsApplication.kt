package ru.sandello.binaryconverter

import android.app.Application
import ru.sandello.binaryconverter.utils.Converter
import ru.sandello.binaryconverter.utils.Shared.converter

class NumberSystemsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        converter = Converter()
    }
}