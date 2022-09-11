package ru.sandello.binaryconverter.ui.calculator

import android.annotation.SuppressLint
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

@HiltViewModel
class CalculatorViewModel @Inject constructor(private val converter: Converter) : ViewModel() {
    private val _numberSystemCustom1 = mutableStateOf(NumberSystem(String(), Radix(10)))
    val numberSystemCustom1: State<NumberSystem> = _numberSystemCustom1
    private val _numberSystemCustom2 = mutableStateOf(NumberSystem(String(), Radix(2)))
    val numberSystemCustom2: State<NumberSystem> = _numberSystemCustom2
    private val _numberSystemResult = mutableStateOf(NumberSystem(String(), Radix(10)))
    val numberSystemResult: State<NumberSystem> = _numberSystemResult

    private val _radixCalculation = mutableStateOf(Radix(10))
    val radixCalculation: State<Radix> = _radixCalculation

    private val _selectedArithmetic = mutableStateOf(Addition)
    val selectedArithmetic: State<ArithmeticType> = _selectedArithmetic

    private val numberSystem1Temp = mutableStateOf(NumberSystem(String(), _radixCalculation.value))
    private val numberSystem2Temp = mutableStateOf(NumberSystem(String(), _radixCalculation.value))
    private val numberSystemResultTemp = mutableStateOf(NumberSystem(String(), _radixCalculation.value))

    private val _numberSystem1error = mutableStateOf(false)
    val numberSystem1error: State<Boolean> = _numberSystem1error
    private val _numberSystem2error = mutableStateOf(false)
    val numberSystem2error: State<Boolean> = _numberSystem2error

    val hasData: State<Boolean>
        get() = mutableStateOf(_numberSystemCustom1.value.value.isNotBlank() || _numberSystemCustom2.value.value.isNotBlank())

    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }
    val arithmeticOptions = listOf(Addition, Subtraction, Multiply, Divide)

    private var lastValueFrom: NumberSystem? = null

    fun convertFrom(operandType: OperandType, from: NumberSystem) {
        convert(operandType, from, toRadixes = arrayOf(_radixCalculation.value))
    }

    fun updateRadix(radixType: RadixType, newRadix: Radix) {
        when (radixType) {
            RadixCustom1 -> {
                _numberSystemCustom1.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom1.radix from ${_numberSystemCustom1.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandCustom1, from = _numberSystemCustom1.value, toRadixes = arrayOf(radixCalculation.value))
            }
            RadixCustom2 -> {
                _numberSystemCustom2.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemCustom2.radix from ${_numberSystemCustom2.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandCustom2, from = _numberSystemCustom2.value, toRadixes = arrayOf(radixCalculation.value))
            }
            RadixResult -> {
                _numberSystemResult.value.run {
                    if (radix == newRadix) return
                    radix = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: numberSystemResult.radix from ${_numberSystemResult.value.radix.value} to ${newRadix.value}")
                calculate()
            }
            RadixCalculation -> {
                _radixCalculation.run {
                    if (value == newRadix) return
                    value = newRadix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: radixCalculation.value from ${_radixCalculation.value.value} to ${newRadix.value}")
                calculate()
            }
        }
    }

    fun selectArithmetic(arithmeticType: ArithmeticType) {
        Log.d(APP_TAG, "CalculatorViewModel::selectArithmetic: arithmeticType: $arithmeticType")

        _selectedArithmetic.run {
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
                _numberSystemCustom1.value = from
            }
            OperandCustom2 -> {
                _numberSystemCustom2.value = from
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
                OperandCustom1 -> _numberSystem1error.value = true
                OperandCustom2 -> _numberSystem2error.value = true
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
                _numberSystemCustom1.value.value = tempValue
            }
            OperandCustom2 -> {
                _numberSystemCustom2.value.value = tempValue
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
                        OperandResult -> _numberSystemResult.value = convertedData.result
                    }
                }
        }
    }

    private fun calculate() {
        if (numberSystem1Temp.value.value.isBlank() || numberSystem2Temp.value.value.isBlank()) {
            return
        }
        Log.d(APP_TAG, "CalculatorViewModel::calculate")

        numberSystemResultTemp.value.value = when (_selectedArithmetic.value) {
            Addition -> (numberSystem1Temp.value.value.toBigDecimal().plus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Subtraction -> (numberSystem1Temp.value.value.toBigDecimal().minus(numberSystem2Temp.value.value.toBigDecimal())).toString()
            Multiply -> (numberSystem1Temp.value.value.toBigDecimal().multiply(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            Divide -> {
                if (numberSystem2Temp.value.value.toFloatOrNull() == 0.0f) {
                    _numberSystem2error.value = true
                    return
                }
                (numberSystem1Temp.value.value.toBigDecimal().divide(numberSystem2Temp.value.value.toBigDecimal(), DECIMAL128)).toString()
            }
        }

        convert(operandType = OperandResult, from = numberSystemResultTemp.value, toRadixes = arrayOf(_numberSystemResult.value.radix))
    }

    fun clear() {
        _numberSystemCustom1.value = _numberSystemCustom1.value.copy(value = String())
        _numberSystemCustom2.value = _numberSystemCustom2.value.copy(value = String())
        _numberSystemResult.value = _numberSystemResult.value.copy(value = String())
        resetErrors()
    }

    private fun resetErrors() {
        _numberSystem1error.value = false
        _numberSystem2error.value = false
    }

}
