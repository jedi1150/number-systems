package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.sandello.binaryconverter.utils.Converter
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(private val converter: Converter) : ViewModel() {
    private val numberSystem10 = mutableStateOf(NumberSystem(String(), Radix.DEC))
    private val numberSystem2 = mutableStateOf(NumberSystem(String(), Radix.BIN))
    private val numberSystem8 = mutableStateOf(NumberSystem(String(), Radix.OCT))
    private val numberSystem16 = mutableStateOf(NumberSystem(String(), Radix.HEX))
    private val numberSystemCustom = mutableStateOf(NumberSystem(String(), Radix(3)))

    private val numberSystem10error = mutableStateOf(false)
    private val numberSystem2error = mutableStateOf(false)
    private val numberSystem8error = mutableStateOf(false)
    private val numberSystem16error = mutableStateOf(false)
    private val numberSystemCustomError = mutableStateOf(false)

    private var lastNumberSystem: NumberSystem? = null

    private val _showExplanation = mutableStateOf(false)
    val showExplanation: State<Boolean> = _showExplanation

    private val _converterUiState = mutableStateOf(
        ConverterUiState(
            numberSystem10 = numberSystem10,
            numberSystem2 = numberSystem2,
            numberSystem8 = numberSystem8,
            numberSystem16 = numberSystem16,
            numberSystemCustom = numberSystemCustom,
            numberSystem10error = numberSystem10error,
            numberSystem2error = numberSystem2error,
            numberSystem8error = numberSystem8error,
            numberSystem16error = numberSystem16error,
            numberSystemCustomError = numberSystemCustomError,
        ),
    )
    val converterUiState: State<ConverterUiState> = _converterUiState

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(numberSystem2.value.radix, numberSystem8.value.radix, numberSystem10.value.radix, numberSystem16.value.radix, numberSystemCustom.value.radix))
    }

    @OptIn(FlowPreview::class)
    private fun convert(from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "ConverterViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) {
            clear()
            return
        }

        check(from.value.matches(CharRegex().charsRegex(
            index = from.radix.value,
            useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
            useNegativeChar = from.value.count { it.toString().contains("[-]".toRegex()) } <= 1,
        ))) {
            Log.d(APP_TAG, "ConverterViewModel::convert: Invalid character entered")
            when (from.radix) {
                numberSystem2.value.radix -> numberSystem2error.value = true
                numberSystem8.value.radix -> numberSystem8error.value = true
                numberSystem10.value.radix -> numberSystem10error.value = true
                numberSystem16.value.radix -> numberSystem16error.value = true
                numberSystemCustom.value.radix -> numberSystemCustomError.value = true
            }
            return
        }

        lastNumberSystem = from

        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes.filter { _toRadix ->
                (from.radix != _toRadix).also {
                    if (!it) {
                        when (_toRadix) {
                            numberSystem2.value.radix -> {
                                if (numberSystem2.value.value == from.value) cancel()
                                numberSystem2.value = from
                            }
                            numberSystem8.value.radix -> {
                                if (numberSystem8.value.value == from.value) cancel()
                                numberSystem8.value = from
                            }
                            numberSystem10.value.radix -> {
                                if (numberSystem10.value.value == from.value) cancel()
                                numberSystem10.value = from
                            }
                            numberSystem16.value.radix -> {
                                if (numberSystem16.value.value == from.value) cancel()
                                numberSystem16.value = from
                            }
                            numberSystemCustom.value.radix -> {
                                if (numberSystemCustom.value.value == from.value) cancel()
                                numberSystemCustom.value = from
                            }
                        }
                    }
                }
            }.asFlow().flatMapMerge { _toRadix -> converter(from = from, toRadix = _toRadix) }.onCompletion { cause ->
                if (cause != null) {
                    Log.e(APP_TAG, "Flow completed exceptionally: $cause")
                } else {
                    resetErrors()
                }
            }.catch { error -> Log.e(APP_TAG, "ConverterViewModel::convert: catch", error) }.collect { convertedData ->
                when (convertedData.result.radix) {
                    numberSystem2.value.radix -> numberSystem2.value = convertedData.result
                    numberSystem8.value.radix -> numberSystem8.value = convertedData.result
                    numberSystem10.value.radix -> numberSystem10.value = convertedData.result
                    numberSystem16.value.radix -> numberSystem16.value = convertedData.result
                    numberSystemCustom.value.radix -> numberSystemCustom.value = convertedData.result
                }
            }
        }
    }

    fun updateCustomRadix(newRadix: Radix) {
        Log.d(APP_TAG, "ConverterViewModel::updateCustomRadix ${numberSystemCustom.value.radix} to $newRadix")

        val tempNS = lastNumberSystem
        if (tempNS != null && numberSystemCustom.value.radix != newRadix) {
            if (lastNumberSystem?.radix == numberSystemCustom.value.radix) {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(numberSystem2.value.radix, numberSystem8.value.radix, numberSystem10.value.radix, numberSystem16.value.radix),
                )
            } else {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(newRadix),
                )
            }
        }

        numberSystemCustom.value.radix = newRadix
    }

    fun showExplanation() {
        _showExplanation.value = true
    }

    fun hideExplanation() {
        _showExplanation.value = false
    }

    fun clear() {
        numberSystem10.value = numberSystem10.value.copy(value = String())
        numberSystem2.value = numberSystem2.value.copy(value = String())
        numberSystem8.value = numberSystem8.value.copy(value = String())
        numberSystem16.value = numberSystem16.value.copy(value = String())
        numberSystemCustom.value = numberSystemCustom.value.copy(value = String())
        resetErrors()
    }

    private fun resetErrors() {
        numberSystem10error.value = false
        numberSystem2error.value = false
        numberSystem8error.value = false
        numberSystem16error.value = false
        numberSystemCustomError.value = false
    }

}
