package ru.sandello.binaryconverter.utils

import ru.sandello.binaryconverter.numsys.NumSys
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.numsys.model.groupLength

fun String.pretty(radix: Radix, isDigitGroupingEnabled: Boolean): String {
    return this.split(NumSys.Constants.DELIMITER).joinToString(separator = NumSys.Constants.DELIMITER.toString()) { part ->
        part.reversed().chunked(radix.groupLength()).joinToString(if (isDigitGroupingEnabled) NumSys.Constants.GROUP_SEPARATOR.toString() else "").reversed()
    }
}
