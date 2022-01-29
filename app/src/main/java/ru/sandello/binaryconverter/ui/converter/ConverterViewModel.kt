package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
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

    private val _operand10error = mutableStateOf(false)
    val operand10error: State<Boolean>
        get() = _operand10error
    private val _operand2error = mutableStateOf(false)
    val operand2error: State<Boolean>
        get() = _operand2error
    private val _operand8error = mutableStateOf(false)
    val operand8error: State<Boolean>
        get() = _operand8error
    private val _operand16error = mutableStateOf(false)
    val operand16error: State<Boolean>
        get() = _operand16error
    private val _operandCustomError = mutableStateOf(false)
    val operandCustomError: State<Boolean>
        get() = _operandCustomError

    private val radixes = IntArray(36) { it + 1 }
    val customRadixes = radixes.toMutableList().filter { !listOf(1, 2, 8, 10, 16).contains(it) }

    private var lastValueFrom: TextFieldValue? = null
    private var lastRadixFrom: Int? = null

    fun convertFrom(textFieldValue: TextFieldValue, fromRadix: Int) {
        convert(textFieldValue, fromRadix, toRadixes = intArrayOf(2, 8, 10, 16, _customRadix.value))
    }

    @OptIn(FlowPreview::class)
    fun convert(textFieldValue: TextFieldValue, fromRadix: Int, @IntRange(from = 2, to = 36) toRadixes: IntArray) {
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
            when (fromRadix) {
                2 -> _operand2error.value = true
                8 -> _operand8error.value = true
                10 -> _operand10error.value = true
                16 -> _operand16error.value = true
                _customRadix.value -> _operandCustomError.value = true
            }
            return
        }

        lastValueFrom = textFieldValue
        lastRadixFrom = fromRadix

        var tempValue = textFieldValue

        if (tempValue.text.contains("-".toRegex())) {
            tempValue = textFieldValue.copy(tempValue.text.replace("-", "").replaceRange(0, 0, "-"))
        }

        viewModelScope.launch {
            toRadixes
                .filter { _toRadix ->
                    (fromRadix != _toRadix).also {
                        if (!it) {
                            when (_toRadix) {
                                2 -> {
                                    if (_operand2.value.text == textFieldValue.text) cancel()
                                    _operand2.value = tempValue
                                }
                                8 -> {
                                    if (_operand8.value.text == textFieldValue.text) cancel()
                                    _operand8.value = tempValue
                                }
                                10 -> {
                                    if (_operand10.value.text == textFieldValue.text) cancel()
                                    _operand10.value = tempValue
                                }
                                16 -> {
                                    if (_operand16.value.text == textFieldValue.text) cancel()
                                    _operand16.value = tempValue
                                }
                                _customRadix.value -> {
                                    if (_operandCustom.value.text == textFieldValue.text) cancel()
                                    _operandCustom.value = tempValue
                                }
                            }
                        }
                    }
                }
                .asFlow()
                .flatMapMerge { _toRadix -> converter(value = tempValue.text, fromRadix = fromRadix, toRadix = _toRadix) }
                .onCompletion { cause ->
                    if (cause != null) {
                        Log.d(APP_TAG, "Flow completed exceptionally")
                    } else {
                        resetErrors()
                    }
                }
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
        Log.d(APP_TAG, "ConverterViewModel::updateCustomRadix ${_customRadix.value} to $newRadix")

        val value = lastValueFrom
        val fromRadix = lastRadixFrom
        if (value != null && fromRadix != null && _customRadix.value != newRadix) {
            if (fromRadix == _customRadix.value) {
                convert(
                    textFieldValue = TextFieldValue().copy(text = value.text),
                    fromRadix = newRadix,
                    toRadixes = intArrayOf(2, 8, 10, 16),
                )
            } else {
                convert(
                    textFieldValue = TextFieldValue().copy(text = value.text),
                    fromRadix = fromRadix,
                    toRadixes = intArrayOf(newRadix),
                )
            }
        }

        _customRadix.value = newRadix
    }

    fun clear() {
        _operand10.value = TextFieldValue()
        _operand2.value = TextFieldValue()
        _operand8.value = TextFieldValue()
        _operand16.value = TextFieldValue()
        _operandCustom.value = TextFieldValue()
        resetErrors()
    }

    private fun resetErrors() {
        _operand10error.value = false
        _operand2error.value = false
        _operand8error.value = false
        _operand16error.value = false
        _operandCustomError.value = false
    }

}
