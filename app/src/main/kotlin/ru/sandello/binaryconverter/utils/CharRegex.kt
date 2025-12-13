package ru.sandello.binaryconverter.utils

class CharRegex {
    private val chars = "0123456789abcdefghijklmnopqrstuvwxyz"
    private val negativeChars = "-"
    private val delimiterChars = ",."

    fun charsRegex(index: Int, useDelimiterChars: Boolean, useNegativeChar: Boolean): Regex {
        var resultRegex: String = chars.take(index)
        if (useDelimiterChars) resultRegex += delimiterChars
        if (useNegativeChar) resultRegex += negativeChars
        return "^[${resultRegex}]*\$".toRegex(RegexOption.IGNORE_CASE)
    }
}
