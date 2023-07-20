package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
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
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(private val numSys: NumSys) : ViewModel() {

    private var lastNumberSystem: NumberSystem? = null

    private val _showExplanation = mutableStateOf(false)
    val showExplanation: State<Boolean> = _showExplanation

    private val _converterUiState: MutableStateFlow<ConverterUiState> = MutableStateFlow(ConverterUiState())
    val converterUiState: StateFlow<ConverterUiState> = _converterUiState.asStateFlow()

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(converterUiState.value.numberSystem2.radix, converterUiState.value.numberSystem8.radix, converterUiState.value.numberSystem10.radix, converterUiState.value.numberSystem16.radix, converterUiState.value.numberSystemCustom.radix))
    }

    private fun convert(from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "ConverterViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) {
            clear()
            return
        }

        check(from.value.matches(CharRegex().charsRegex(
            index = from.radix.value,
            useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
            useNegativeChar = from.value.count { it.toString().contains("-".toRegex()) } <= 1,
        ))) {
            Log.w(APP_TAG, "ConverterViewModel::convert: Invalid character entered")

            when (from.radix) {
                converterUiState.value.numberSystem2.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem2Error = true)
                converterUiState.value.numberSystem8.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem8Error = true)
                converterUiState.value.numberSystem10.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem10Error = true)
                converterUiState.value.numberSystem16.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem16Error = true)
                converterUiState.value.numberSystemCustom.radix -> _converterUiState.value = converterUiState.value.copy(numberSystemCustomError = true)
            }
            return
        }

        lastNumberSystem = from

        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes.filter { radix ->
                (from.radix != radix).also {
                    if (!it) {
                        when (radix) {
                            converterUiState.value.numberSystem2.radix -> {
                                if (converterUiState.value.numberSystem2.value == from.value) cancel()
                                _converterUiState.value = converterUiState.value.copy(numberSystem2 = from)
                            }

                            converterUiState.value.numberSystem8.radix -> {
                                if (converterUiState.value.numberSystem8.value == from.value) cancel()
                                _converterUiState.value = converterUiState.value.copy(numberSystem8 = from)
                            }

                            converterUiState.value.numberSystem10.radix -> {
                                if (converterUiState.value.numberSystem10.value == from.value) cancel()
                                _converterUiState.value = converterUiState.value.copy(numberSystem10 = from)
                            }

                            converterUiState.value.numberSystem16.radix -> {
                                if (converterUiState.value.numberSystem16.value == from.value) cancel()
                                _converterUiState.value = converterUiState.value.copy(numberSystem16 = from)
                            }

                            converterUiState.value.numberSystemCustom.radix -> {
                                if (converterUiState.value.numberSystemCustom.value == from.value) cancel()
                                _converterUiState.value = converterUiState.value.copy(numberSystemCustom = from)
                            }
                        }
                    }
                }
            }.map { toRadix ->
                try {
                    numSys.convert(value = from, toRadix = toRadix)
                } catch (exception: IllegalArgumentException) {
                    cancel()
                    return@launch
                }
            }.asFlow().onCompletion { cause ->
                if (cause != null) {
                    Log.e(APP_TAG, "Flow completed exceptionally: $cause")
                } else {
                    resetErrors()
                }
            }.catch { error -> Log.e(APP_TAG, "ConverterViewModel::convert: catch", error) }.collect { convertedData ->
                when (convertedData.radix) {
                    converterUiState.value.numberSystem2.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem2 = convertedData)
                    converterUiState.value.numberSystem8.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem8 = convertedData)
                    converterUiState.value.numberSystem10.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem10 = convertedData)
                    converterUiState.value.numberSystem16.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem16 = convertedData)
                    converterUiState.value.numberSystemCustom.radix -> _converterUiState.value = converterUiState.value.copy(numberSystemCustom = convertedData)
                }
            }
        }
    }

    fun updateCustomRadix(newRadix: Radix) {
        Log.d(APP_TAG, "ConverterViewModel::updateCustomRadix ${converterUiState.value.numberSystemCustom.radix} to $newRadix")

        val tempNS = lastNumberSystem
        if (tempNS != null && converterUiState.value.numberSystemCustom.radix != newRadix) {
            if (lastNumberSystem?.radix == converterUiState.value.numberSystemCustom.radix) {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(converterUiState.value.numberSystem2.radix, converterUiState.value.numberSystem8.radix, converterUiState.value.numberSystem10.radix, converterUiState.value.numberSystem16.radix),
                )
            } else {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(newRadix),
                )
            }
        }

        converterUiState.value.numberSystemCustom.radix = newRadix
    }

    fun showExplanation() {
        _showExplanation.value = true
    }

    fun hideExplanation() {
        _showExplanation.value = false
    }

    fun clear() {
        _converterUiState.value = ConverterUiState(
            numberSystemCustom = NumberSystem(value = String(), radix = converterUiState.value.numberSystemCustom.radix),
        )
    }

    private fun resetErrors() {
        _converterUiState.value = converterUiState.value.copy(
            numberSystem2Error = false,
            numberSystem8Error = false,
            numberSystem10Error = false,
            numberSystem16Error = false,
            numberSystemCustomError = false,
        )
    }

}
