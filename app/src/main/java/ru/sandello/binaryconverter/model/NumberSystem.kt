package ru.sandello.binaryconverter.model

import ru.sandello.binaryconverter.utils.NS_DELIMITER
import ru.sandello.binaryconverter.utils.NS_GROUP_SEPARATOR

data class NumberSystem(var value: String, var radix: Radix)

fun NumberSystem.formattedValue(): String {
    return this.value.split(NS_DELIMITER).joinToString(separator = NS_DELIMITER.toString()) { part ->
        part.reversed().chunked(this.groupLength()).joinToString(NS_GROUP_SEPARATOR.toString()).reversed()
    }
}
