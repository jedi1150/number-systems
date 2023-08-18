package ru.sandello.binaryconverter.utils

fun getFractional(value: String): String {
    return "0." + value.substringAfter(NS_DELIMITER)
}
