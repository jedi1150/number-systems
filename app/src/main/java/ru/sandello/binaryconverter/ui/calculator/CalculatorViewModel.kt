package ru.sandello.binaryconverter.ui.calculator

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import numsys.NumSys
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Addition
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Divide
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Multiply
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.Subtraction
import ru.sandello.binaryconverter.ui.calculator.CalculatorOperandType.OperandCustom1
import ru.sandello.binaryconverter.ui.calculator.CalculatorOperandType.OperandCustom2
import ru.sandello.binaryconverter.ui.calculator.CalculatorOperandType.OperandResult
import ru.sandello.binaryconverter.ui.calculator.CalculatorRadixType.RadixCalculation
import ru.sandello.binaryconverter.ui.calculator.CalculatorRadixType.RadixCustom1
import ru.sandello.binaryconverter.ui.calculator.CalculatorRadixType.RadixCustom2
import ru.sandello.binaryconverter.ui.calculator.CalculatorRadixType.RadixResult
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import java.math.MathContext.DECIMAL128
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val numSys: NumSys) : ViewModel() {
    private val radixCalculation = mutableStateOf(Radix.DEC)
    private val numberSystem1Temp = mutableStateOf(NumberSystem(String(), radixCalculation.value))
    private val numberSystem2Temp = mutableStateOf(NumberSystem(String(), radixCalculation.value))
    private val numberSystemResultTemp = mutableStateOf(NumberSystem(String(), radixCalculation.value))

    private val _calculatorUiState: MutableStateFlow<CalculatorUiState> = MutableStateFlow(CalculatorUiState())
    val calculatorUiState: StateFlow<CalculatorUiState> = _calculatorUiState.asStateFlow()

    private var lastValueFrom: NumberSystem? = null

    fun convertFrom(calculatorOperandType: CalculatorOperandType, from: NumberSystem) {
        convert(calculatorOperandType, from, toRadixes = arrayOf(radixCalculation.value))
    }

    fun updateRadix(calculatorRadixType: CalculatorRadixType, newRadix: Radix) {
        when (calculatorRadixType) {
            RadixCustom1 -> {
                calculatorUiState.value.numberSystemCustom1.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom1.radix from ${calculatorUiState.value.numberSystemCustom1.radix.value} to ${newRadix.value}")
                convert(calculatorOperandType = OperandCustom1, from = calculatorUiState.value.numberSystemCustom1, toRadixes = arrayOf(radixCalculation.value))
            }

            RadixCustom2 -> {
                calculatorUiState.value.numberSystemCustom2.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom2.radix from ${calculatorUiState.value.numberSystemCustom2.radix.value} to ${newRadix.value}")
                convert(calculatorOperandType = OperandCustom2, from = calculatorUiState.value.numberSystemCustom2, toRadixes = arrayOf(radixCalculation.value))
            }

            RadixResult -> {
                calculatorUiState.value.numberSystemResult.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemResult.radix from ${calculatorUiState.value.numberSystemResult.radix.value} to ${newRadix.value}")
                calculate()
            }

            RadixCalculation -> {
                radixCalculation.run {
                    if (value == newRadix) return
                    value = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: radixCalculation.value from ${radixCalculation.value.value} to ${newRadix.value}")
                calculate()
            }
        }
    }

    fun selectArithmetic(arithmeticType: ArithmeticType) {
        Log.d(APP_TAG, "CalculatorViewModel::selectArithmetic: arithmeticType: $arithmeticType")

        calculatorUiState.value.selectedArithmetic.run {
            if (this == arithmeticType) return
            _calculatorUiState.value = calculatorUiState.value.copy(selectedArithmetic = arithmeticType)
            calculate()
        }
    }

    private fun convert(calculatorOperandType: CalculatorOperandType, from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "CalculatorViewModel::convert: textFieldVal: ${from.value}, from radix: ${from.radix.value}")

        check(from.value.matches(CharRegex().charsRegex(
            index = from.radix.value,
            useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
            useNegativeChar = from.value.count { it.toString().contains("-".toRegex()) } <= 1,
        ))) {
            Log.w(APP_TAG, "CalculatorViewModel::convert: Invalid character entered")
            when (calculatorOperandType) {
                OperandCustom1 -> _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom1Error = true)
                OperandCustom2 -> _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom2Error = true)
                else -> {}
            }
            return
        }

        lastValueFrom = from

        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes.filter { radix ->
                (from.radix != radix).also {
                    if (!it) {
                        when (calculatorOperandType) {
                            OperandCustom1 -> {
                                _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom1 = calculatorUiState.value.numberSystemCustom1.copy(value = from.value))
                                numberSystem1Temp.value.value = from.value
                                cancel()
                            }

                            OperandCustom2 -> {
                                _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom2 = calculatorUiState.value.numberSystemCustom2.copy(value = from.value))
                                numberSystem2Temp.value.value = from.value
                                cancel()
                            }

                            OperandResult -> {
                                _calculatorUiState.value = calculatorUiState.value.copy(numberSystemResult = calculatorUiState.value.numberSystemResult.copy(value = from.value))
                                cancel()
                            }
                        }
                    } else {
                        when (calculatorOperandType) {
                            OperandCustom1 -> {
                                _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom1 = calculatorUiState.value.numberSystemCustom1.copy(value = from.value))
                            }

                            OperandCustom2 -> {
                                _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom2 = calculatorUiState.value.numberSystemCustom2.copy(value = from.value))
                            }

                            else -> {}
                        }
                    }
                }
            }.map { toRadix ->
                try {
                    numSys.convert(value = from, toRadix = toRadix)
                } catch (exception: IllegalArgumentException) {
                    when (calculatorOperandType) {
                        OperandCustom1 -> numberSystem1Temp.value = numberSystem1Temp.value.copy(value = String())
                        OperandCustom2 -> numberSystem2Temp.value = numberSystem2Temp.value.copy(value = String())
                        else -> {}
                    }
                    _calculatorUiState.value = calculatorUiState.value.copy(numberSystemResult = calculatorUiState.value.numberSystemResult.copy(value = String()))
                    cancel()
                    return@launch
                }
            }.asFlow().onCompletion { cause ->
                if (cause != null) {
                    Log.d(APP_TAG, "Flow completed exceptionally")
                } else {
                    resetErrors()
                    if (calculatorOperandType != OperandResult) calculate()
                }
            }.catch { error -> Log.e(APP_TAG, "CalculatorViewModel::convert: catch", error) }.collect { convertedData ->
                when (calculatorOperandType) {
                    OperandCustom1 -> numberSystem1Temp.value = convertedData
                    OperandCustom2 -> numberSystem2Temp.value = convertedData
                    OperandResult -> _calculatorUiState.value = calculatorUiState.value.copy(numberSystemResult = convertedData)
                }
            }
        }
    }

    private fun calculate() {
        if (numberSystem1Temp.value.value.isBlank() || numberSystem2Temp.value.value.isBlank()) {
            _calculatorUiState.value = calculatorUiState.value.copy(numberSystemResult = calculatorUiState.value.numberSystemResult.copy(value = String()))
            return
        }
        Log.d(APP_TAG, "CalculatorViewModel::calculate")

        numberSystemResultTemp.value.value = when (calculatorUiState.value.selectedArithmetic) {
            Addition -> (numberSystem1Temp.value.value.toBigDecimal().plus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Subtraction -> (numberSystem1Temp.value.value.toBigDecimal().minus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Multiply -> (numberSystem1Temp.value.value.toBigDecimal().multiply(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            Divide -> {
                if (numberSystem2Temp.value.value.toFloatOrNull() == 0.0f) {
                    _calculatorUiState.value = calculatorUiState.value.copy(numberSystemCustom2Error = true)
                    return
                }
                (numberSystem1Temp.value.value.toBigDecimal().divide(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            }
        }

        convert(calculatorOperandType = OperandResult, from = numberSystemResultTemp.value, toRadixes = arrayOf(calculatorUiState.value.numberSystemResult.radix))
    }

    fun clear() {
        _calculatorUiState.value = CalculatorUiState(
            numberSystemCustom1 = NumberSystem(value = String(), radix = calculatorUiState.value.numberSystemCustom1.radix),
            numberSystemCustom2 = NumberSystem(value = String(), radix = calculatorUiState.value.numberSystemCustom2.radix),
            numberSystemResult = NumberSystem(value = String(), radix = calculatorUiState.value.numberSystemResult.radix),
            selectedArithmetic = calculatorUiState.value.selectedArithmetic,
        )
        numberSystem1Temp.value.value = String()
        numberSystem2Temp.value.value = String()
        numberSystemResultTemp.value.value = String()
    }

    private fun resetErrors() {
        _calculatorUiState.value = calculatorUiState.value.copy(
            numberSystemCustom1Error = false,
            numberSystemCustom2Error = false,
        )
    }

}
