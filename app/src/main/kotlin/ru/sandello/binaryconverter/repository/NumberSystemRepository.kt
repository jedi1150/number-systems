package ru.sandello.binaryconverter.repository

import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.numsys.NumberSystemDataSource
import javax.inject.Inject

class NumberSystemRepository @Inject constructor(
    private val numberSystemDataSource: NumberSystemDataSource,
) {

    suspend fun convert(value: NumberSystem, toRadix: Radix): NumberSystem? = numberSystemDataSource.convert(value, toRadix)

}
