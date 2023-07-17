package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.asFlow
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

    var converterUiState by mutableStateOf(ConverterUiState())
        private set

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(converterUiState.numberSystem2.radix, converterUiState.numberSystem8.radix, converterUiState.numberSystem10.radix, converterUiState.numberSystem16.radix, converterUiState.numberSystemCustom.radix))
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
                converterUiState.numberSystem2.radix -> converterUiState = converterUiState.copy(numberSystem2Error = true)
                converterUiState.numberSystem8.radix -> converterUiState = converterUiState.copy(numberSystem8Error = true)
                converterUiState.numberSystem10.radix -> converterUiState = converterUiState.copy(numberSystem10Error = true)
                converterUiState.numberSystem16.radix -> converterUiState = converterUiState.copy(numberSystem16Error = true)
                converterUiState.numberSystemCustom.radix -> converterUiState = converterUiState.copy(numberSystemCustomError = true)
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
                            converterUiState.numberSystem2.radix -> {
                                if (converterUiState.numberSystem2.value == from.value) cancel()
                                converterUiState = converterUiState.copy(numberSystem2 = from)
                            }

                            converterUiState.numberSystem8.radix -> {
                                if (converterUiState.numberSystem8.value == from.value) cancel()
                                converterUiState = converterUiState.copy(numberSystem8 = from)
                            }

                            converterUiState.numberSystem10.radix -> {
                                if (converterUiState.numberSystem10.value == from.value) cancel()
                                converterUiState = converterUiState.copy(numberSystem10 = from)
                            }
                            converterUiState.numberSystem16.radix -> {
                                if (converterUiState.numberSystem16.value == from.value) cancel()
                                converterUiState = converterUiState.copy(numberSystem16 = from)
                            }

                            converterUiState.numberSystemCustom.radix -> {
                                if (converterUiState.numberSystemCustom.value == from.value) cancel()
                                converterUiState = converterUiState.copy(numberSystemCustom = from)
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
                    converterUiState.numberSystem2.radix -> converterUiState = converterUiState.copy(numberSystem2 = convertedData)
                    converterUiState.numberSystem8.radix -> converterUiState = converterUiState.copy(numberSystem8 = convertedData)
                    converterUiState.numberSystem10.radix -> converterUiState = converterUiState.copy(numberSystem10 = convertedData)
                    converterUiState.numberSystem16.radix -> converterUiState = converterUiState.copy(numberSystem16 = convertedData)
                    converterUiState.numberSystemCustom.radix -> converterUiState = converterUiState.copy(numberSystemCustom = convertedData)
                }
            }
        }
    }

    fun updateCustomRadix(newRadix: Radix) {
        Log.d(APP_TAG, "ConverterViewModel::updateCustomRadix ${converterUiState.numberSystemCustom.radix} to $newRadix")

        val tempNS = lastNumberSystem
        if (tempNS != null && converterUiState.numberSystemCustom.radix != newRadix) {
            if (lastNumberSystem?.radix == converterUiState.numberSystemCustom.radix) {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(converterUiState.numberSystem2.radix, converterUiState.numberSystem8.radix, converterUiState.numberSystem10.radix, converterUiState.numberSystem16.radix),
                )
            } else {
                convert(
                    from = tempNS,
                    toRadixes = arrayOf(newRadix),
                )
            }
        }

        converterUiState.numberSystemCustom.radix = newRadix
    }

    fun showExplanation() {
        _showExplanation.value = true
    }

    fun hideExplanation() {
        _showExplanation.value = false
    }

    fun clear() {
        converterUiState = ConverterUiState()
    }

    private fun resetErrors() {
        converterUiState = converterUiState.copy(
            numberSystem2Error = false,
            numberSystem8Error = false,
            numberSystem10Error = false,
            numberSystem16Error = false,
            numberSystemCustomError = false,
        )
    }

}
