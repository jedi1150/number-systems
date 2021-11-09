package ru.sandello.binaryconverter.utils

class CharRegex {
    private val chars = "01234567890abcdefghijklmnopqrstuvwxyz"
    private val specialChars = "-,."

    fun charsRegex(index: Int): Regex =
        "^[${specialChars}${chars.substring(0, index)}]*\$".toRegex()
}
