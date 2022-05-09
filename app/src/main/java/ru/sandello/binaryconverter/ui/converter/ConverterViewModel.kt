package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.annotation.IntRange
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Shared.converter

class ConverterViewModel : ViewModel() {

    private val _operand10 = mutableStateOf(String())
    val operand10: State<String> = _operand10
    private val _operand2 = mutableStateOf(String())
    val operand2: State<String> = _operand2
    private val _operand8 = mutableStateOf(String())
    val operand8: State<String> = _operand8
    private val _operand16 = mutableStateOf(String())
    val operand16: State<String> = _operand16
    private val _operandCustom = mutableStateOf(String())
    val operandCustom: State<String> = _operandCustom
    private val _customRadix = mutableStateOf(3)
    val customRadix: State<Int> = _customRadix

    private val _operand10error = mutableStateOf(false)
    val operand10error: State<Boolean> = _operand10error
    private val _operand2error = mutableStateOf(false)
    val operand2error: State<Boolean> = _operand2error
    private val _operand8error = mutableStateOf(false)
    val operand8error: State<Boolean> = _operand8error
    private val _operand16error = mutableStateOf(false)
    val operand16error: State<Boolean> = _operand16error
    private val _operandCustomError = mutableStateOf(false)
    val operandCustomError: State<Boolean> = _operandCustomError

    val hasData: State<Boolean>
        get() = mutableStateOf(_operand10.value.isNotBlank() || _operand2.value.isNotBlank() || _operand8.value.isNotBlank() || _operand16.value.isNotBlank() || _operandCustom.value.isNotBlank())

    val radixes = IntArray(36) { radix -> radix + 1 }.filter { !listOf(1, 2, 8, 10, 16).contains(it) }

    private var lastValueFrom: String? = null
    private var lastRadixFrom: Int? = null

    fun convertFrom(fromValue: String, fromRadix: Int) {
        convert(fromValue, fromRadix, toRadixes = intArrayOf(2, 8, 10, 16, _customRadix.value))
    }

    @OptIn(FlowPreview::class)
    private fun convert(fromValue: String, fromRadix: Int, @IntRange(from = 2, to = 36) toRadixes: IntArray) {
        Log.d(APP_TAG, "ConverterViewModel::convert: textFieldVal: $fromValue, from radix: $fromRadix")

        if (fromValue.isEmpty()) {
            clear()
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

        lastValueFrom = fromValue
        lastRadixFrom = fromRadix

        var tempValue = fromValue

        if (tempValue.contains("-".toRegex())) {
            tempValue = tempValue.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes
                .filter { _toRadix ->
                    (fromRadix != _toRadix).also {
                        if (!it) {
                            when (_toRadix) {
                                2 -> {
                                    if (_operand2.value == fromValue) cancel()
                                    _operand2.value = tempValue
                                }
                                8 -> {
                                    if (_operand8.value == fromValue) cancel()
                                    _operand8.value = tempValue
                                }
                                10 -> {
                                    if (_operand10.value == fromValue) cancel()
                                    _operand10.value = tempValue
                                }
                                16 -> {
                                    if (_operand16.value == fromValue) cancel()
                                    _operand16.value = tempValue
                                }
                                _customRadix.value -> {
                                    if (_operandCustom.value == fromValue) cancel()
                                    _operandCustom.value = tempValue
                                }
                            }
                        }
                    }
                }
                .asFlow()
                .flatMapMerge { _toRadix -> converter(value = tempValue, fromRadix = fromRadix, toRadix = _toRadix) }
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
                        2 -> _operand2.value = convertedData.result
                        8 -> _operand8.value = convertedData.result
                        10 -> _operand10.value = convertedData.result
                        16 -> _operand16.value = convertedData.result
                        _customRadix.value -> _operandCustom.value = convertedData.result
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
                    fromValue = value,
                    fromRadix = newRadix,
                    toRadixes = intArrayOf(2, 8, 10, 16),
                )
            } else {
                convert(
                    fromValue = value,
                    fromRadix = fromRadix,
                    toRadixes = intArrayOf(newRadix),
                )
            }
        }

        _customRadix.value = newRadix
    }

    fun clear() {
        _operand10.value = String()
        _operand2.value = String()
        _operand8.value = String()
        _operand16.value = String()
        _operandCustom.value = String()
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
