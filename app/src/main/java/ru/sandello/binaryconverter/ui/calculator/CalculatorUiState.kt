package ru.sandello.binaryconverter.ui.calculator

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix

enum class OperandType {
    OperandCustom1,
    OperandCustom2,
    OperandResult,
}

enum class RadixType {
    RadixCustom1,
    RadixCustom2,
    RadixResult,
    RadixCalculation,
}

enum class ArithmeticType {
    Addition,
    Subtraction,
    Multiply,
    Divide,
}

data class CalculatorUiState(
    val numberSystemCustom1: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.DEC)),
    val numberSystemCustom2: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.BIN)),
    val numberSystemResult: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.OCT)),
    val numberSystemCustom1error: State<Boolean> = mutableStateOf(false),
    val numberSystemCustom2error: State<Boolean> = mutableStateOf(false),
    val selectedArithmetic: State<ArithmeticType> = mutableStateOf(ArithmeticType.Addition),
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }

    val arithmeticTypes = listOf(ArithmeticType.Addition, ArithmeticType.Subtraction, ArithmeticType.Multiply, ArithmeticType.Divide)

    val hasData: State<Boolean>
        get() = mutableStateOf(numberSystemCustom1.value.value.isNotBlank() || numberSystemCustom2.value.value.isNotBlank())
}
