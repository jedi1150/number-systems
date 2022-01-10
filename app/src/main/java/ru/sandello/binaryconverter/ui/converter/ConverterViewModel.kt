package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Shared.converter

class ConverterViewModel : ViewModel() {

    private val _operand10 = mutableStateOf(TextFieldValue())
    val operand10: State<TextFieldValue>
        get() = _operand10
    private val _operand2 = mutableStateOf(TextFieldValue())
    val operand2: State<TextFieldValue>
        get() = _operand2
    private val _operand8 = mutableStateOf(TextFieldValue())
    val operand8: State<TextFieldValue>
        get() = _operand8
    private val _operand16 = mutableStateOf(TextFieldValue())
    val operand16: State<TextFieldValue>
        get() = _operand16
    private val _operandCustom = mutableStateOf(TextFieldValue())
    val operandCustom: State<TextFieldValue>
        get() = _operandCustom
    private val _customRadix = mutableStateOf(3)
    val customRadix: State<Int>
        get() = _customRadix

    private val radixes = IntArray(36) { it + 1 }
    val customRadixes = radixes.toMutableList().filter { !listOf(1, 2, 8, 10, 16).contains(it) }

    @OptIn(FlowPreview::class)
    fun convert(textFieldValue: TextFieldValue, fromRadix: Int) {
        Log.d(APP_TAG, "ConverterViewModel::convert: textFieldVal: $textFieldValue, from radix: $fromRadix")

        if (textFieldValue.text.isEmpty()) {
            clear()
            return
        }

        check(
            textFieldValue.text.matches(
                CharRegex().charsRegex(
                    index = fromRadix,
                    useDelimiterChars = textFieldValue.text.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = textFieldValue.text.count { it.toString().contains("[-]".toRegex()) } <= 1,
                )
            )
        ) {
            Log.d(APP_TAG, "ConverterViewModel::convert: Invalid character entered")
            return
        }

        var tempValue = textFieldValue

        if (tempValue.text.contains("-".toRegex())) {
            tempValue = textFieldValue.copy(tempValue.text.replace("-", "").replaceRange(0, 0, "-"))
        }

        viewModelScope.launch {
            intArrayOf(2, 8, 10, 16, _customRadix.value)
                .filter { _toRadix ->
                    (fromRadix != _toRadix).also {
                        if (!it) {
                            when (_toRadix) {
                                2 -> _operand2.value = tempValue
                                8 -> _operand8.value = tempValue
                                10 -> _operand10.value = tempValue
                                16 -> _operand16.value = tempValue
                                _customRadix.value -> _operandCustom.value = tempValue
                            }
                        }
                    }
                }
                .asFlow()
                .flatMapMerge { _toRadix -> converter(value = tempValue.text, fromRadix = fromRadix, toRadix = _toRadix) }
                .onCompletion { cause -> if (cause != null) Log.d(APP_TAG, "Flow completed exceptionally") }
                .catch { error -> Log.e(APP_TAG, "ConverterViewModel::convert: catch", error) }
                .collect { convertedData ->
                    when (convertedData.toRadix) {
                        2 -> _operand2.value = TextFieldValue(convertedData.result)
                        8 -> _operand8.value = TextFieldValue(convertedData.result)
                        10 -> _operand10.value = TextFieldValue(convertedData.result)
                        16 -> _operand16.value = TextFieldValue(convertedData.result)
                        _customRadix.value -> _operandCustom.value = TextFieldValue(convertedData.result)
                    }
                }
        }
    }

    fun updateCustomRadix(newRadix: Int) {
        Log.i(APP_TAG, "ConverterViewModel::updateCustomRadix ${_customRadix.value} to $newRadix")
        _customRadix.value = newRadix
    }

    fun clear() {
        _operand10.value = TextFieldValue()
        _operand2.value = TextFieldValue()
        _operand8.value = TextFieldValue()
        _operand16.value = TextFieldValue()
        _operandCustom.value = TextFieldValue()
    }

}
