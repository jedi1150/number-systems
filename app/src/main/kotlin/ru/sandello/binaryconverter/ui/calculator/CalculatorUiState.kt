package ru.sandello.binaryconverter.ui.calculator

import android.annotation.SuppressLint
import ru.sandello.binaryconverter.numsys.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix

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
    val radixes: List<Radix> = (2..36).map { Radix(it) }

    val arithmeticTypes = arrayOf(ArithmeticType.Addition, ArithmeticType.Subtraction, ArithmeticType.Multiply, ArithmeticType.Divide)

    val hasData: Boolean
        get() = numberSystemCustom1.value.isNotBlank() || numberSystemCustom2.value.isNotBlank()
}
