package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import android.util.Log
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
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Shared.converter

class ConverterViewModel : ViewModel() {

    private val _numberSystem10 = mutableStateOf(NumberSystem(String(), Radix(10)))
    val numberSystem10: State<NumberSystem> = _numberSystem10
    private val _numberSystem2 = mutableStateOf(NumberSystem(String(), Radix(12)))
    val numberSystem2: State<NumberSystem> = _numberSystem2
    private val _numberSystem8 = mutableStateOf(NumberSystem(String(), Radix(8)))
    val numberSystem8: State<NumberSystem> = _numberSystem8
    private val _numberSystem16 = mutableStateOf(NumberSystem(String(), Radix(16)))
    val numberSystem16: State<NumberSystem> = _numberSystem16
    private val _numberSystemCustom = mutableStateOf(NumberSystem(String(), Radix(3)))
    val numberSystemCustom: State<NumberSystem> = _numberSystemCustom

    private val _numberSystem10error = mutableStateOf(false)
    val numberSystem10error: State<Boolean> = _numberSystem10error
    private val _numberSystem2error = mutableStateOf(false)
    val numberSystem2error: State<Boolean> = _numberSystem2error
    private val _numberSystem8error = mutableStateOf(false)
    val numberSystem8error: State<Boolean> = _numberSystem8error
    private val _numberSystem16error = mutableStateOf(false)
    val numberSystem16error: State<Boolean> = _numberSystem16error
    private val _numberSystemCustomError = mutableStateOf(false)
    val numberSystemCustomError: State<Boolean> = _numberSystemCustomError

    val hasData: State<Boolean>
        get() = mutableStateOf(_numberSystem10.value.value.isNotBlank() || _numberSystem2.value.value.isNotBlank() || _numberSystem8.value.value.isNotBlank() || _numberSystem16.value.value.isNotBlank() || _numberSystemCustom.value.value.isNotBlank())

    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { !listOf(Radix(1), _numberSystem2.value.radix, _numberSystem8.value.radix, _numberSystem10.value.radix, _numberSystem16.value.radix).contains(it) }

    private var lastNumberSystem: NumberSystem? = null

    private val _showExplanation = mutableStateOf(false)
    val showExplanation: State<Boolean> = _showExplanation

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(_numberSystem2.value.radix, _numberSystem8.value.radix, _numberSystem10.value.radix, _numberSystem16.value.radix, _numberSystemCustom.value.radix))
    }

    @OptIn(FlowPreview::class)
    private fun convert(from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "ConverterViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) {
            clear()
            return
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
            Log.d(APP_TAG, "ConverterViewModel::convert: Invalid character entered")
            when (from.radix) {
                _numberSystem2.value.radix -> _numberSystem2error.value = true
                _numberSystem8.value.radix -> _numberSystem8error.value = true
                _numberSystem10.value.radix -> _numberSystem10error.value = true
                _numberSystem16.value.radix -> _numberSystem16error.value = true
                _numberSystemCustom.value.radix -> _numberSystemCustomError.value = true
            }
            return
        }

        lastNumberSystem = from

        var tempValue = from

        if (tempValue.value.contains("-".toRegex())) {
            tempValue.value = tempValue.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes
                .filter { _toRadix ->
                    (from.radix != _toRadix).also {
                        if (!it) {
                            when (_toRadix) {
                                _numberSystem2.value.radix -> {
                                    if (_numberSystem2.value.value == from.value) cancel()
                                    _numberSystem2.value = tempValue
                                }
                                _numberSystem8.value.radix -> {
                                    if (_numberSystem8.value.value == from.value) cancel()
                                    _numberSystem8.value = tempValue
                                }
                                _numberSystem10.value.radix -> {
                                    if (_numberSystem10.value.value == from.value) cancel()
                                    _numberSystem10.value = tempValue
                                }
                                _numberSystem16.value.radix -> {
                                    if (_numberSystem16.value.value == from.value) cancel()
                                    _numberSystem16.value = tempValue
                                }
                                _numberSystemCustom.value.radix -> {
                                    if (_numberSystemCustom.value.value == from.value) cancel()
                                    _numberSystemCustom.value = tempValue
                                }
                            }
                        }
                    }
                }
                .asFlow()
                .flatMapMerge { _toRadix -> converter(from = tempValue, toRadix = _toRadix) }
                .onCompletion { cause ->
                    if (cause != null) {
                        Log.e(APP_TAG, "Flow completed exceptionally: $cause")
                    } else {
                        resetErrors()
                    }
                }
                .catch { error -> Log.e(APP_TAG, "ConverterViewModel::convert: catch", error) }
                .collect { convertedData ->
                    when (convertedData.result.radix) {
                        _numberSystem2.value.radix -> _numberSystem2.value = convertedData.result
                        _numberSystem8.value.radix -> _numberSystem8.value = convertedData.result
                        _numberSystem10.value.radix -> _numberSystem10.value = convertedData.result
                        _numberSystem16.value.radix -> _numberSystem16.value = convertedData.result
                        _numberSystemCustom.value.radix -> _numberSystemCustom.value = convertedData.result
                    }
                }
        }
    }

    fun updateCustomRadix(newRadix: Radix) {
        Log.d(APP_TAG, "ConverterViewModel::updateCustomRadix ${_numberSystemCustom.value.radix} to $newRadix")

        val tempNS = lastNumberSystem
        if (tempNS != null && _numberSystemCustom.value.radix != newRadix) {
            if (lastNumberSystem?.radix == _numberSystemCustom.value.radix) {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(_numberSystem2.value.radix, _numberSystem8.value.radix, _numberSystem10.value.radix, _numberSystem16.value.radix),
                )
            } else {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(newRadix),
                )
            }
        }

        _numberSystemCustom.value.radix = newRadix
    }

    fun showExplanation() {
        _showExplanation.value = true
    }

    fun hideExplanation() {
        _showExplanation.value = false
    }

    fun clear() {
        _numberSystem10.value = _numberSystem10.value.copy(value = String())
        _numberSystem2.value = _numberSystem2.value.copy(value = String())
        _numberSystem8.value = _numberSystem8.value.copy(value = String())
        _numberSystem16.value = _numberSystem16.value.copy(value = String())
        _numberSystemCustom.value = _numberSystemCustom.value.copy(value = String())
        resetErrors()
    }

    private fun resetErrors() {
        _numberSystem10error.value = false
        _numberSystem2error.value = false
        _numberSystem8error.value = false
        _numberSystem16error.value = false
        _numberSystemCustomError.value = false
    }

}
