package ru.sandello.binaryconverter.numsys

import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

public object NumSys {
    public var fractionalLength: Int = 12
    private const val DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

    public fun convert(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
        ignoreCase: Boolean = false,
    ): String {
        require(value.isNotBlank()) { "Source value must not be empty" }
        require(value.all { it == '.' || it.isLetterOrDigit() }) { "Incorrect source value" }
        require(sourceRadix in 2..62 && targetRadix in 2..62) { "Source and target radixes must be in the range from 2 to 62" }

        val parts = if (ignoreCase) value.lowercase().split(".") else value.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""

        val integerResult = convertIntegerPart(integerPart, sourceRadix, targetRadix)
        val fractionalResult = convertFractionalPart(fractionalPart, sourceRadix, targetRadix)

        var result = if (fractionalResult.isEmpty()) integerResult else "$integerResult.$fractionalResult"

        result = result.trimEnd('0').trimEnd('.')

        return result
    }

    public fun convert(
        numberSystem: NumberSystem,
        targetRadix: Radix,
        ignoreCase: Boolean = false,
    ): NumberSystem = NumberSystem(
        value = convert(
            value = numberSystem.value,
            sourceRadix = numberSystem.radix.value,
            targetRadix = targetRadix.value,
            ignoreCase = ignoreCase,
        ),
        radix = targetRadix,
    )

    public fun NumberSystem.toRadix(
        value: Radix,
        ignoreCase: Boolean = false,
    ): NumberSystem = convert(this, value, ignoreCase)

    public fun convertIntegerPartStepByStep(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
    ): Pair<List<String>, String> {
        val decimal = convertToDecimal(value, sourceRadix)
        val steps = mutableListOf<String>()
        var current = decimal
        val radixBI = targetRadix.toBigInteger()
        val converted = StringBuilder()

        while (current > BigInteger.ZERO) {
            val remainder = (current % radixBI).toInt()
            val char = digitToChar(remainder)
            steps.add("$current ÷ $targetRadix = ${current / radixBI}, remainder: $remainder ('$char')")
            converted.insert(0, char)
            current /= radixBI
        }

        if (converted.isEmpty()) converted.append("0")
        return steps to converted.toString()
    }

    public fun convertFractionalPartStepByStep(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
    ): Pair<List<String>, String> {
        val steps = mutableListOf<String>()
        var remaining = convertFractionalToDecimal(value, sourceRadix)
        val bigRadix = targetRadix.toBigDecimal()
        val result = StringBuilder()

        repeat(fractionalLength) {
            remaining *= bigRadix
            val digit = remaining.toInt()
            val char = digitToChar(digit)
            steps.add("${remaining.toPlainString()} × $targetRadix = ${remaining.toPlainString()}, digit: $digit ('$char')")
            result.append(char)
            remaining -= digit.toBigDecimal()
        }

        return steps to result.toString().trimEnd('0')
    }

    public fun convertStepByStep(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
        ignoreCase: Boolean = false,
    ): Pair<List<String>, String> {
        require(value.isNotBlank()) { "Source value must not be empty" }

        val parts = if (ignoreCase) value.lowercase().split(".") else value.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""

        val integerSteps = convertIntegerPartStepByStep(integerPart, sourceRadix, targetRadix)
        val fractionalSteps = if (fractionalPart.isNotEmpty()) {
            convertFractionalPartStepByStep(fractionalPart, sourceRadix, targetRadix)
        } else null

        val allSteps = mutableListOf<String>()
        allSteps += integerSteps.first

        if (fractionalSteps != null) {
            allSteps += fractionalSteps.first
        }

        val result = if (fractionalSteps != null && fractionalSteps.second.isNotEmpty()) {
            "${integerSteps.second}.${fractionalSteps.second}"
        } else {
            integerSteps.second
        }

        return allSteps to result
    }

    private fun convertIntegerPart(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
    ): String {
        val decimalValue = convertToDecimal(value, sourceRadix)
        return convertFromDecimal(decimalValue, targetRadix)
    }

    private fun convertFractionalPart(
        value: String,
        sourceRadix: Int,
        targetRadix: Int,
    ): String {
        val decimalValue = convertFractionalToDecimal(value, sourceRadix)
        return convertFractionalFromDecimal(decimalValue, targetRadix)
    }

    private fun convertToDecimal(value: String, radix: Int): BigInteger {
        var result = BigInteger.ZERO
        value.forEach { char ->
            val digit = charToDigit(char)
            require(digit < radix) { "Digit '$char' out of bounds for radix $radix" }
            result = result * radix.toBigInteger() + digit.toBigInteger()
        }
        return result
    }

    private fun convertFractionalToDecimal(value: String, radix: Int): BigDecimal {
        var result = BigDecimal.ZERO
        val bigRadix = radix.toBigDecimal()
        var divisor = bigRadix

        value.forEach { char ->
            val digit = charToDigit(char)
            require(digit < radix) { "Digit '$char' out of bounds for radix $radix" }
            result += digit.toBigDecimal().divide(divisor, fractionalLength + 5, RoundingMode.HALF_EVEN)
            divisor *= bigRadix
        }

        return result
    }

    private fun convertFromDecimal(value: BigInteger, radix: Int): String {
        var decimalValue = value
        val result = StringBuilder()
        while (decimalValue > BigInteger.ZERO) {
            val remainder = (decimalValue % radix.toBigInteger()).toInt()
            result.insert(0, digitToChar(remainder))
            decimalValue /= radix.toBigInteger()
        }
        return if (result.isEmpty()) "0" else result.toString()
    }

    private fun convertFractionalFromDecimal(value: BigDecimal, radix: Int): String {
        val result = StringBuilder()
        var remainingValue = value
        val bigRadix = radix.toBigDecimal()
        repeat(fractionalLength) {
            remainingValue *= bigRadix
            val integerPart = remainingValue.toInt()
            result.append(digitToChar(integerPart))
            remainingValue -= integerPart.toBigDecimal()
        }
        return result.toString()
    }

    private fun charToDigit(c: Char): Int {
        val index = DIGITS.indexOf(c)
        require(index >= 0) { "Invalid character '$c'" }
        return index
    }

    private fun digitToChar(d: Int): Char {
        require(d in 0 until DIGITS.length) { "Invalid digit value: $d" }
        return DIGITS[d]
    }

    public object Constants {
        public const val COMMA: Char = ','
        public const val DELIMITER: Char = '.'
        public const val GROUP_SEPARATOR: Char = ' '
    }
}
