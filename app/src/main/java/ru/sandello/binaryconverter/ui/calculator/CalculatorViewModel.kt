package ru.sandello.binaryconverter.ui.calculator

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.*
import ru.sandello.binaryconverter.ui.calculator.OperandType.*
import ru.sandello.binaryconverter.ui.calculator.RadixType.*
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Converter
import java.math.MathContext.DECIMAL128
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val converter: Converter) : ViewModel() {
    private val numberSystemCustom1 = mutableStateOf(NumberSystem(String(), Radix.DEC))
    private val numberSystemCustom2 = mutableStateOf(NumberSystem(String(), Radix.BIN))
    private val numberSystemResult = mutableStateOf(NumberSystem(String(), Radix.DEC))

    private val numberSystem1error = mutableStateOf(false)
    private val numberSystem2error = mutableStateOf(false)

    private val selectedArithmetic = mutableStateOf(Addition)

    private val radixCalculation = mutableStateOf(Radix.DEC)
    private val numberSystem1Temp = mutableStateOf(NumberSystem(String(), radixCalculation.value))
    private val numberSystem2Temp = mutableStateOf(NumberSystem(String(), radixCalculation.value))
    private val numberSystemResultTemp = mutableStateOf(NumberSystem(String(), radixCalculation.value))

    private val _calculatorUiState = mutableStateOf(
        CalculatorUiState(
            numberSystemCustom1 = numberSystemCustom1,
            numberSystemCustom2 = numberSystemCustom2,
            numberSystemResult = numberSystemResult,
            numberSystemCustom1error = numberSystem1error,
            numberSystemCustom2error = numberSystem2error,
            selectedArithmetic = selectedArithmetic,
        ),
    )
    val calculatorUiState: State<CalculatorUiState> = _calculatorUiState

    private var lastValueFrom: NumberSystem? = null

    fun convertFrom(operandType: OperandType, from: NumberSystem) {
        convert(operandType, from, toRadixes = arrayOf(radixCalculation.value))
    }

    fun updateRadix(radixType: RadixType, newRadix: Radix) {
        when (radixType) {
            RadixCustom1 -> {
                numberSystemCustom1.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom1.radix from ${numberSystemCustom1.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandCustom1, from = numberSystemCustom1.value, toRadixes = arrayOf(radixCalculation.value))
            }
            RadixCustom2 -> {
                numberSystemCustom2.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom2.radix from ${numberSystemCustom2.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandCustom2, from = numberSystemCustom2.value, toRadixes = arrayOf(radixCalculation.value))
            }
            RadixResult -> {
                numberSystemResult.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemResult.radix from ${numberSystemResult.value.radix.value} to ${newRadix.value}")
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

        selectedArithmetic.run {
            if (value == arithmeticType) return
            value = arithmeticType
            calculate()
        }
    }


    @OptIn(FlowPreview::class)
    private fun convert(operandType: OperandType, from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "CalculatorViewModel::convert: textFieldVal: ${from.value}, from radix: ${from.radix.value}")

        when (operandType) {
            OperandCustom1 -> {
                numberSystemCustom1.value = from
            }
            OperandCustom2 -> {
                numberSystemCustom2.value = from
            }
            else -> {}
        }

        check(
            from.value.matches(
                CharRegex().charsRegex(
                    index = from.radix.value,
                    useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = from.value.count { it.toString().contains("[-]".toRegex()) } <= 1,
                )
            )
        ) {
            Log.e(APP_TAG, "CalculatorViewModel::convert: Invalid character entered")
            when (operandType) {
                OperandCustom1 -> numberSystem1error.value = true
                OperandCustom2 -> numberSystem2error.value = true
                else -> {}
            }
            return
        }

        lastValueFrom = from

        var tempValue = from.value

        if (tempValue.contains("-".toRegex())) {
            tempValue = tempValue.replace("-", "").replaceRange(0, 0, "-")
        }

        when (operandType) {
            OperandCustom1 -> {
                numberSystemCustom1.value.value = tempValue
            }
            OperandCustom2 -> {
                numberSystemCustom2.value.value = tempValue
            }
            else -> {}
        }

        viewModelScope.launch {
            toRadixes
                .asFlow()
                .flatMapMerge { _toRadix -> converter(from = from, toRadix = _toRadix) }
                .onCompletion { cause ->
                    if (cause != null) {
                        Log.d(APP_TAG, "Flow completed exceptionally")
                    } else {
                        resetErrors()
                        if (operandType != OperandResult) calculate()
                    }
                }
                .catch { error -> Log.e(APP_TAG, "CalculatorViewModel::convert: catch", error) }
                .collect { convertedData ->
                    Log.d(APP_TAG, "CalculatorViewModel::collect: operandType: $operandType, result: ${convertedData.result}")
                    when (operandType) {
                        OperandCustom1 -> numberSystem1Temp.value = convertedData.result
                        OperandCustom2 -> numberSystem2Temp.value = convertedData.result
                        OperandResult -> numberSystemResult.value = convertedData.result
                    }
                }
        }
    }

    private fun calculate() {
        if (numberSystem1Temp.value.value.isBlank() || numberSystem2Temp.value.value.isBlank()) {
            return
        }
        Log.d(APP_TAG, "CalculatorViewModel::calculate")

        numberSystemResultTemp.value.value = when (selectedArithmetic.value) {
            Addition -> (numberSystem1Temp.value.value.toBigDecimal().plus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Subtraction -> (numberSystem1Temp.value.value.toBigDecimal().minus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Multiply -> (numberSystem1Temp.value.value.toBigDecimal().multiply(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            Divide -> {
                if (numberSystem2Temp.value.value.toFloatOrNull() == 0.0f) {
                    numberSystem2error.value = true
                    return
                }
                (numberSystem1Temp.value.value.toBigDecimal().divide(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            }
        }

        convert(operandType = OperandResult, from = numberSystemResultTemp.value, toRadixes = arrayOf(numberSystemResult.value.radix))
    }

    fun clear() {
        numberSystemCustom1.value = numberSystemCustom1.value.copy(value = String())
        numberSystemCustom2.value = numberSystemCustom2.value.copy(value = String())
        numberSystemResult.value = numberSystemResult.value.copy(value = String())
        resetErrors()
    }

    private fun resetErrors() {
        numberSystem1error.value = false
        numberSystem2error.value = false
    }

}
