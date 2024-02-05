package ru.sandello.binaryconverter.utils

import numsys.model.Radix
import numsys.model.groupLength

fun String.pretty(radix: Radix): String {
    return this.split(numsys.utils.NS_DELIMITER).joinToString(separator = numsys.utils.NS_DELIMITER.toString()) { part ->
        part.reversed().chunked(radix.groupLength()).joinToString(numsys.utils.NS_GROUP_SEPARATOR.toString()).reversed()
    }
}
