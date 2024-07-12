package ru.sandello.binaryconverter.ui.calculator

import android.annotation.SuppressLint
import numsys.model.NumberSystem
import numsys.model.Radix

enum class CalculatorOperandType { OperandCustom1, OperandCustom2, OperandResult }

enum class CalculatorRadixType { RadixCustom1, RadixCustom2, RadixResult, RadixCalculation }

enum class ArithmeticType { Addition, Subtraction, Multiply, Divide }

data class CalculatorUiState(
    val numberSystemCustom1: NumberSystem = NumberSystem(String(), Radix.DEC),
    val numberSystemCustom2: NumberSystem = NumberSystem(String(), Radix.BIN),
    val numberSystemResult: NumberSystem = NumberSystem(String(), Radix.OCT),
    val numberSystemCustom1Error: Boolean = false,
    val numberSystemCustom2Error: Boolean = false,
    val selectedArithmetic: ArithmeticType = ArithmeticType.Addition,
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }

    val arithmeticTypes = arrayOf(ArithmeticType.Addition, ArithmeticType.Subtraction, ArithmeticType.Multiply, ArithmeticType.Divide)

    val hasData: Boolean
        get() = numberSystemCustom1.value.isNotBlank() || numberSystemCustom2.value.isNotBlank()
}
