package ru.sandello.binaryconverter.utils

fun getFractional(value: String): String = "0." + value.substringAfter(NS_DELIMITER)
