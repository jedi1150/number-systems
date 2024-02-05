package ru.sandello.binaryconverter.numsys

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import numsys.NumSys
import numsys.model.NumberSystem
import numsys.model.Radix
import javax.inject.Inject

class NumberSystemDataSource @Inject constructor(
    private val numSys: NumSys,
) {
    suspend fun convert(from: NumberSystem, toRadix: Radix): NumberSystem? = coroutineScope {
        Log.d("ConverterViewModel", "convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) return@coroutineScope null

        return@coroutineScope withContext(Dispatchers.Default) { numSys.convert(value = from, toRadix = toRadix) }
    }
}
