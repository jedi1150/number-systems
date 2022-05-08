package ru.sandello.binaryconverter.ui.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.ui.calculator.ArithmeticType.*
import ru.sandello.binaryconverter.ui.calculator.OperandType.*
import ru.sandello.binaryconverter.ui.calculator.RadixType.*
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Shared

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

class CalculatorViewModel : ViewModel() {
    private val _operandCustom1 = mutableStateOf(String())
    val operandCustom1: State<String>
        get() = _operandCustom1
    private val _radixCustom1 = mutableStateOf(10)
    val radixCustom1: State<Int>
        get() = _radixCustom1
    private val _operandCustom2 = mutableStateOf(String())
    val operandCustom2: State<String>
        get() = _operandCustom2
    private val _radixCustom2 = mutableStateOf(2)
    val radixCustom2: State<Int>
        get() = _radixCustom2
    private val _operandResult = mutableStateOf(String())
    val operandResult: State<String>
        get() = _operandResult
    private val _radixResult = mutableStateOf(10)
    val radixResult: State<Int>
        get() = _radixResult
    private val _radixCalculation = mutableStateOf(10)
    val radixCalculation: State<Int>
        get() = _radixCalculation

    private val _selectedArithmetic = mutableStateOf(Addition)
    val selectedArithmetic: State<ArithmeticType> = _selectedArithmetic

    private val operandCustom1Temp = mutableStateOf(String())
    private val operandCustom2Temp = mutableStateOf(String())
    private val operandResultTemp = mutableStateOf(String())

    private val _operandCustom1error = mutableStateOf(false)
    val operandCustom1error: State<Boolean>
        get() = _operandCustom1error

    private val _operandCustom2error = mutableStateOf(false)
    val operandCustom2error: State<Boolean>
        get() = _operandCustom2error

    val radixes = IntArray(36) { it + 1 }
    val arithmeticOptions = listOf(Addition, Subtraction, Multiply, Divide)

    val showInvalidInputError = MutableLiveData<Pair<Int, String>>()
    val stringToast = MutableLiveData<String>()

    private var myClip: ClipData? = null
    private var myClipboard: ClipboardManager? = null

    private var lastValueFrom: String? = null
    private var lastRadixFrom: Int? = null

    fun convertFrom(operandType: OperandType, fromValue: String, fromRadix: Int) {
        convert(operandType, fromValue, fromRadix, toRadixes = intArrayOf(_radixCalculation.value))
    }

    fun updateRadix(radixType: RadixType, radix: Int) {
        when (radixType) {
            RadixCustom1 -> {
                _radixCustom1.run {
                    if (value == radix) return
                    value = radix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: customRadix1 from ${_radixCustom1.value} to $radix")
                convert(operandType = OperandCustom1, fromValue = _operandCustom1.value, fromRadix = _radixCustom1.value, toRadixes = intArrayOf(radixCalculation.value))
            }
            RadixCustom2 -> {
                _radixCustom2.run {
                    if (value == radix) return
                    value = radix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: customRadix2 from ${_radixCustom2.value} to $radix")
                convert(operandType = OperandCustom2, fromValue = _operandCustom2.value, fromRadix = _radixCustom2.value, toRadixes = intArrayOf(radixCalculation.value))
            }
            RadixResult -> {
                _radixResult.run {
                    if (value == radix) return
                    value = radix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: radixResult from ${_radixResult.value} to $radix")
                calculate()
            }
            RadixCalculation -> {
                _radixCalculation.run {
                    if (value == radix) return
                    value = radix
                }
                Log.d(APP_TAG, "CalculatorViewModel::updateRadix: radixCalculation from ${_radixCalculation.value} to $radix")
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
    private fun convert(operandType: OperandType, fromValue: String, fromRadix: Int, @IntRange(from = 2, to = 36) toRadixes: IntArray) {
        Log.d(APP_TAG, "CalculatorViewModel::convert: textFieldVal: $fromValue, from radix: $fromRadix")

        if (fromValue.isEmpty()) {
            return
        }

        check(
            fromValue.matches(
                CharRegex().charsRegex(
                    index = fromRadix,
                    useDelimiterChars = fromValue.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = fromValue.count { it.toString().contains("[-]".toRegex()) } <= 1,
                )
            )
        ) {
            Log.d(APP_TAG, "CalculatorViewModel::convert: Invalid character entered")
            when (operandType) {
                OperandCustom1 -> _operandCustom1error.value = true
                OperandCustom2 -> _operandCustom2error.value = true
            }
            return
        }

        lastValueFrom = fromValue
        lastRadixFrom = fromRadix

        var tempValue = fromValue

        if (tempValue.contains("-".toRegex())) {
            tempValue = tempValue.replace("-", "").replaceRange(0, 0, "-")
        }

        when (operandType) {
            OperandCustom1 -> {
                _operandCustom1.value = tempValue
            }
            OperandCustom2 -> {
                _operandCustom2.value = tempValue
            }
            else -> {}
        }

        viewModelScope.launch {
            toRadixes
                .asFlow()
                .flatMapMerge { _toRadix -> Shared.converter(value = tempValue, fromRadix = fromRadix, toRadix = _toRadix) }
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
                    when (operandType) {
                        OperandCustom1 -> operandCustom1Temp.value = convertedData.result
                        OperandCustom2 -> operandCustom2Temp.value = convertedData.result
                        OperandResult -> _operandResult.value = convertedData.result
                    }
                }
        }
    }

    private fun calculate() {
        if (operandCustom1Temp.value.isEmpty() || operandCustom2Temp.value.isEmpty()) {
            return
        }
        Log.d(APP_TAG, "CalculatorViewModel::calculate")

        operandResultTemp.value = when (_selectedArithmetic.value) {
            Addition -> (operandCustom1Temp.value.toInt() + operandCustom2Temp.value.toInt()).toString()
            Subtraction -> (operandCustom1Temp.value.toInt() - operandCustom2Temp.value.toInt()).toString()
            Multiply -> (operandCustom1Temp.value.toInt() * operandCustom2Temp.value.toInt()).toString()
            Divide -> (operandCustom1Temp.value.toInt() / operandCustom2Temp.value.toInt()).toString()
        }

        convert(operandType = OperandResult, fromValue = operandResultTemp.value, fromRadix = radixCalculation.value, toRadixes = intArrayOf(radixResult.value))
    }

    fun clear() {
        _operandCustom1.value = String()
        _operandCustom2.value = String()
        _operandResult.value = String()
        resetErrors()
    }

    private fun resetErrors() {
        _operandCustom1error.value = false
        _operandCustom2error.value = false
    }


    /*  private fun errorNull() {
          binding.textInputLayoutCustom1.error = null
          binding.textInputLayoutCustom2.error = null
          binding.textInputLayoutResult.error = null
      }*/

    /*private fun checkClear() {
        if (binding.editTextVal1.text.toString() != "" || binding.editTextVal2.text.toString() != "")
            binding.clear_fab.show()
        else
            binding.clear_fab.hide()
    }*/

//    fun copyVal(value: String) {
//        if (value == "calResult" && result.value.toString() != "0" && result.value.toString() != "") {
//            myClip = ClipData.newPlainText("text", result.value.toString())
//            myClipboard!!.setPrimaryClip(myClip!!)
//
//            stringToast.postValue("${Shared.resourcesHelper.getString(R.string.copied)}: ${_result.value}")
//        }
//    }

    fun updateOperand(action: Int, value: String = "", fraction: Int) {
        try {
            when (action) {
//                1 -> _operand1.postValue(Converter().convert(value, fraction, 10).toBigDecimal())
//                2 -> _operand2.postValue(Converter().convert(value, fraction, 10).toBigDecimal())
            }
        } catch (e: Exception) {
//            showInvalidInputError.postValue(Pair(action, allow(fraction)))
        }
        Log.i(
            APP_TAG,
            "CalculatorViewModel::updateOperand act: $action val: $value, frac: $fraction"
        )
    }

//    fun updateFraction(fractionId: Int, newFraction: Int) {
//        when (fractionId) {
//            1 -> _fraction1.postValue(newFraction)
//            2 -> _fraction2.postValue(newFraction)
//            3 -> _fractionResult.postValue(newFraction)
//        }
//
//        Log.i(APP_TAG, "CalculatorViewModel::updateFraction id: $fractionId val: $newFraction")
//    }

//    private fun calculate() {
//        when (_actions.value) {
//            CalcActions.PLUS -> _result.postValue(_operand1.value?.add(_operand2.value))
//            CalcActions.MINUS -> _result.postValue(_operand1.value?.min(_operand2.value))
//            CalcActions.MULTIPLY -> _result.postValue(_operand1.value?.multiply(_operand2.value))
//            CalcActions.DIVIDE -> _result.postValue(
//                operand1.value?.divide(
//                    operand2.value,
//                    _fractionResult.value ?: 10,
//                    RoundingMode.HALF_UP
//                )
//            )
//        }
//    }

//    fun load() {
//        _operand1.postValue(
//            Shared.preferencesHelper.getStringFromPrefs(SHARED_CALC_OPERAND_1)?.toBigDecimal()
//        )
//        _operand2.postValue(
//            Shared.preferencesHelper.getStringFromPrefs(SHARED_CALC_OPERAND_2)?.toBigDecimal()
//        )
//        _actions.postValue(
//            CalcActions.valueOf(
//                Shared.preferencesHelper.getStringFromPrefs(
//                    SHARED_CALC_ACTIONS
//                ) ?: CalcActions.PLUS.toString()
//            )
//        )
//        _fraction1.postValue(Shared.preferencesHelper.getIntFromPrefs(SHARED_CALC_FRACTION_1))
//        _fraction2.postValue(Shared.preferencesHelper.getIntFromPrefs(SHARED_CALC_FRACTION_2))
//        _fractionResult.postValue(
//            Shared.preferencesHelper.getIntFromPrefs(
//                SHARED_CALC_FRACTION_RESULT
//            )
//        )
//    }

//    fun save() {
//        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_OPERAND_1, operand1.value.toString())
//        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_OPERAND_2, operand2.value.toString())
//        Shared.preferencesHelper.stringToPrefs(SHARED_CALC_ACTIONS, actions.value.toString())
//        fraction1.value?.let { Shared.preferencesHelper.intToPrefs(SHARED_CALC_FRACTION_1, it) }
//        fraction2.value?.let { Shared.preferencesHelper.intToPrefs(SHARED_CALC_FRACTION_2, it) }
//        fractionResult.value?.let {
//            Shared.preferencesHelper.intToPrefs(
//                SHARED_CALC_FRACTION_RESULT,
//                it
//            )
//        }
//    }
}