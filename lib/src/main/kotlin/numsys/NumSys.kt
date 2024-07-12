package numsys

import numsys.model.NumberSystem
import numsys.model.Radix
import kotlin.math.pow

public object NumSys {
    /**
     * The length of the fraction part.
     */
    @JvmStatic
    public var fractionalLength: Int = 12

    /**
     * Converts the value of the source number system to the target number system.
     *
     * @param value source value.
     * @param sourceRadix source radix.
     * @param targetRadix required radix.
     * @return value of target radix.
     */
    @JvmStatic
    public fun convert(value: String, sourceRadix: Int, targetRadix: Int): String {
        require(value.isNotBlank()) { "Source value must not be empty" }
        require(value.replace(".", "").all { char -> char.isLetterOrDigit() }) { "Incorrect source value" }
        require(sourceRadix in 2..62 && targetRadix in 2..62) { "Source and target radixes must be in the range from 2 to 62" }

        val parts = value.split(".")
        val integerPart = parts[0]
        val fractionalPart = if (parts.size > 1) parts[1] else ""

        val integerResult = convertIntegerPart(integerPart, sourceRadix, targetRadix)
        val fractionalResult = convertFractionalPart(fractionalPart, sourceRadix, targetRadix)

        var result = if (fractionalResult.isEmpty()) integerResult else "$integerResult.$fractionalResult"

        while (result.length > 1 && result.contains("[,.]".toRegex()) && result.endsWith("0")) {
            result = result.substringBeforeLast("0")
        }

        while (result.length > 1 && result.contains("[,.]".toRegex()) && result.endsWith(".")) {
            result = result.substringBeforeLast(".")
        }

        return result
    }

    /**
     * Converts [NumberSystem] to another [NumberSystem].
     *
     * @param numberSystem Source [NumberSystem].
     * @param targetRadix Required [Radix] for result.
     * @return [NumberSystem] of target radix.
     * @see NumberSystem
     * @see Radix
     */
    @JvmStatic
    public fun convert(numberSystem: NumberSystem, targetRadix: Radix): NumberSystem {
        return NumberSystem(
            value = convert(
                value = numberSystem.value,
                sourceRadix = numberSystem.radix.value,
                targetRadix = targetRadix.value,
            ),
            radix = targetRadix,
        )
    }

    /**
     * Converts [NumberSystem] to another [NumberSystem].
     *
     * @param value Required [Radix] for result.
     * @return [NumberSystem] of target radix.
     * @see NumberSystem
     * @see Radix
     */
    @JvmStatic
    public fun NumberSystem.toRadix(value: Radix): NumberSystem = convert(this, value)

    @JvmStatic
    private fun convertIntegerPart(value: String, sourceRadix: Int, targetRadix: Int): String {
        val decimalValue = convertToDecimal(value, sourceRadix)
        return convertFromDecimal(decimalValue, targetRadix)
    }

    @JvmStatic
    private fun convertFractionalPart(value: String, sourceRadix: Int, targetRadix: Int): String {
        val decimalValue = convertFractionalToDecimal(value, sourceRadix)
        return convertFractionalFromDecimal(decimalValue, targetRadix)
    }

    @JvmStatic
    private fun convertToDecimal(value: String, radix: Int): Double {
        var result = 0.0
        for ((power, i) in (value.length - 1 downTo 0).withIndex()) {
            val digitValue = when {
                value[i].isDigit() -> value[i] - '0'
                value[i] in 'a'..'z' -> value[i] - 'a' + 10
                value[i] in 'A'..'Z' -> value[i] - 'A' + 36
                else -> throw IllegalArgumentException("Incorrect symbol")
            }
            result += digitValue * radix.toDouble().pow(power.toDouble())
        }
        return result
    }

    @JvmStatic
    private fun convertFractionalToDecimal(value: String, radix: Int): Double {
        var result = 0.0
        for (i in value.indices) {
            val digitValue = when {
                value[i].isDigit() -> value[i] - '0'
                value[i] in 'a'..'z' -> value[i] - 'a' + 10
                value[i] in 'A'..'Z' -> value[i] - 'A' + 36
                else -> throw IllegalArgumentException("Incorrect symbol")
            }
            result += digitValue / radix.toDouble().pow((i + 1).toDouble())
        }
        return result
    }

    @JvmStatic
    private fun convertFromDecimal(value: Double, radix: Int): String {
        var decimalValue = value.toLong()
        val result = StringBuilder()
        while (decimalValue > 0) {
            val remainder = (decimalValue % radix).toInt()
            val remainderChar = if (remainder < 10) (remainder + '0'.code).toChar() else if (remainder < 36) (remainder - 10 + 'a'.code).toChar() else (remainder - 36 + 'A'.code).toChar()
            result.insert(0, remainderChar)
            decimalValue /= radix
        }
        return if (result.isEmpty()) "0" else result.toString()
    }

    @JvmStatic
    private fun convertFractionalFromDecimal(value: Double, radix: Int): String {
        val result = StringBuilder()
        var remainingValue = value
        repeat(5) {
            remainingValue *= radix
            val integerPart = remainingValue.toInt()
            result.append(if (integerPart < 10) (integerPart + '0'.code).toChar() else if (integerPart < 36) (integerPart - 10 + 'a'.code).toChar() else (integerPart - 36 + 'A'.code).toChar())
            remainingValue -= integerPart
        }
        return result.toString()
    }

    public object Constants {
        public const val COMMA: Char = ','
        public const val DELIMITER: Char = '.'
        public const val GROUP_SEPARATOR: Char = ' '
    }

}
