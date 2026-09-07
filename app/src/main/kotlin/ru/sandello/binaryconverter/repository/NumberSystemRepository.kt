package ru.sandello.binaryconverter.repository

import javax.inject.Inject
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.NumberSystemDataSource
import ru.sandello.binaryconverter.numsys.model.Radix

class NumberSystemRepository @Inject constructor(
    private val numberSystemDataSource: NumberSystemDataSource,
) {
    suspend fun convert(value: NumberSystem, toRadix: Radix): NumberSystem? = numberSystemDataSource.convert(value, toRadix)
}
