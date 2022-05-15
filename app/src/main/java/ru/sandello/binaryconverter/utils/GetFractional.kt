package ru.sandello.binaryconverter.utils

import java.math.BigDecimal

fun getFractional(value: BigDecimal): String {
    return value.remainder(BigDecimal.ONE).toString()
}

fun getFractional(value: String): String {
    return "0." + value.substringAfter(".")
}
