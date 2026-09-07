package ru.sandello.binaryconverter.numsys

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.asExternalModel
import ru.sandello.binaryconverter.model.asInternalModel
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.utils.AppLog

class NumberSystemDataSource @Inject constructor(
    private val numSys: NumSys,
    private val defaultDispatcher: CoroutineDispatcher,
) {
    suspend fun convert(from: NumberSystem, toRadix: Radix): NumberSystem? = coroutineScope {
        AppLog.d("NumberSystemDataSource", "convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) return@coroutineScope null

        return@coroutineScope withContext(defaultDispatcher) {
            try {
                numSys
                    .convert(
                        numberSystem = from.asInternalModel(),
                        targetRadix = toRadix,
                        ignoreCase = from.radix.value in Radix.BIN.value..36,
                    ).asExternalModel()
            } catch (e: IllegalArgumentException) {
                AppLog.e("NumberSystemDataSource", "Conversion failed: ${e.message}")
                null
            }
        }
    }
}
