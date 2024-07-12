package ru.sandello.binaryconverter.model

import numsys.NumSys
import numsys.model.Radix
import numsys.model.groupLength

data class NumberSystem(
    var value: String,
    var radix: Radix,
    var isError: Boolean = false,
)

fun numsys.model.NumberSystem.asExternalModel() = NumberSystem(
    value = value,
    radix = radix,
)

fun NumberSystem.asInternalModel() = numsys.model.NumberSystem(
    value,
    radix,
)

fun NumberSystem.pretty(): String = value.split(NumSys.Constants.DELIMITER).joinToString(separator = NumSys.Constants.DELIMITER.toString()) { part ->
    part.reversed().chunked(asInternalModel().groupLength()).joinToString(NumSys.Constants.GROUP_SEPARATOR.toString()).reversed()
}
