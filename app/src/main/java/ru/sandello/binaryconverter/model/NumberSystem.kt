package ru.sandello.binaryconverter.model

data class NumberSystem(var value: String, var radix: Radix)

fun NumberSystem.formattedValue(): String {
    return this.value.split(".").joinToString(separator = ".") { part ->
        part.reversed().chunked(this.groupLength()).joinToString(" ").reversed()
    }
}
