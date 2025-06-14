package ru.sandello.binaryconverter.model

import ru.sandello.binaryconverter.numsys.NumSys
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.numsys.model.groupLength

data class NumberSystem(
    var value: String,
    var radix: Radix,
    var isError: Boolean = false,
)

fun ru.sandello.binaryconverter.numsys.model.NumberSystem.asExternalModel() = NumberSystem(
    value = value,
    radix = radix,
)

fun NumberSystem.asInternalModel() = ru.sandello.binaryconverter.numsys.model.NumberSystem(
    value,
    radix,
)

fun NumberSystem.pretty(isDigitGroupingEnabled: Boolean = true): String = value.split(NumSys.Constants.DELIMITER).joinToString(separator = NumSys.Constants.DELIMITER.toString()) { part ->
    part.reversed().chunked(asInternalModel().groupLength()).joinToString(if (isDigitGroupingEnabled) NumSys.Constants.GROUP_SEPARATOR.toString() else "").reversed()
}
