package ru.sandello.binaryconverter.ui.converter

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix
import ru.sandello.binaryconverter.repository.NumberSystemRepository
import ru.sandello.binaryconverter.repository.SettingsRepository
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val numberSystemRepository: NumberSystemRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private var lastNumberSystem: NumberSystem? = null

    private val _showExplanation = mutableStateOf(false)
    val showExplanation: State<Boolean> = _showExplanation

    private val _converterUiState: MutableStateFlow<ConverterUiState> = MutableStateFlow(ConverterUiState())
    val converterUiState: StateFlow<ConverterUiState> = _converterUiState.asStateFlow()

    val isDigitGroupingEnabled: Flow<Boolean> = settingsRepository.settingsData.map { it.isDigitGroupingEnabled }

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(converterUiState.value.numberSystem2.radix, converterUiState.value.numberSystem8.radix, converterUiState.value.numberSystem10.radix, converterUiState.value.numberSystem16.radix, converterUiState.value.numberSystemCustom.radix))
    }

    private fun convert(from: NumberSystem, toRadixes: Array<Radix>) {
        if (from.value.isEmpty()) {
            clear()
            return
        }

        check(
            from.value.matches(
                CharRegex().charsRegex(
                    index = from.radix.value,
                    useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = false,
                ),
            ),
        ) {
            Log.w(APP_TAG, "ConverterViewModel::convert: Invalid character entered")

            when (from.radix) {
                converterUiState.value.numberSystem2.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem2 = _converterUiState.value.numberSystem2.copy(isError = true))
                converterUiState.value.numberSystem8.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem8 = _converterUiState.value.numberSystem8.copy(isError = true))
                converterUiState.value.numberSystem10.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem10 = _converterUiState.value.numberSystem10.copy(isError = true))
                converterUiState.value.numberSystem16.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem16 = _converterUiState.value.numberSystem16.copy(isError = true))
                converterUiState.value.numberSystemCustom.radix -> _converterUiState.value = converterUiState.value.copy(numberSystemCustom = _converterUiState.value.numberSystemCustom.copy(isError = true))
            }
            return
        }

        lastNumberSystem = from

        resetErrors()

        viewModelScope.launch {
            val convertedResults = toRadixes.mapNotNull { toRadix ->
                if (from.radix == toRadix) {
                    when (toRadix) {
                        converterUiState.value.numberSystem2.radix -> {
                            if (converterUiState.value.numberSystem2.value == from.value) return@mapNotNull null
                            _converterUiState.value = converterUiState.value.copy(numberSystem2 = from)
                        }

                        converterUiState.value.numberSystem8.radix -> {
                            if (converterUiState.value.numberSystem8.value == from.value) return@mapNotNull null
                            _converterUiState.value = converterUiState.value.copy(numberSystem8 = from)
                        }

                        converterUiState.value.numberSystem10.radix -> {
                            if (converterUiState.value.numberSystem10.value == from.value) return@mapNotNull null
                            _converterUiState.value = converterUiState.value.copy(numberSystem10 = from)
                        }

                        converterUiState.value.numberSystem16.radix -> {
                            if (converterUiState.value.numberSystem16.value == from.value) return@mapNotNull null
                            _converterUiState.value = converterUiState.value.copy(numberSystem16 = from)
                        }

                        converterUiState.value.numberSystemCustom.radix -> {
                            if (converterUiState.value.numberSystemCustom.value == from.value) return@mapNotNull null
                            _converterUiState.value = converterUiState.value.copy(numberSystemCustom = from)
                        }
                    }
                    return@mapNotNull null
                } else if (from.radix != toRadix) {
                    return@mapNotNull viewModelScope.async {
                        numberSystemRepository.convert(from, toRadix)
                    }
                } else {
                    return@mapNotNull null
                }
            }.awaitAll()

            convertedResults.filterNotNull().forEach { convertedNumberSystem ->
                when (convertedNumberSystem.radix) {
                    converterUiState.value.numberSystem2.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem2 = convertedNumberSystem)
                    converterUiState.value.numberSystem8.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem8 = convertedNumberSystem)
                    converterUiState.value.numberSystem10.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem10 = convertedNumberSystem)
                    converterUiState.value.numberSystem16.radix -> _converterUiState.value = converterUiState.value.copy(numberSystem16 = convertedNumberSystem)
                    converterUiState.value.numberSystemCustom.radix -> _converterUiState.value = converterUiState.value.copy(numberSystemCustom = convertedNumberSystem)
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
        lastNumberSystem = null

        resetErrors()
        _converterUiState.value = ConverterUiState(
            numberSystemCustom = NumberSystem(value = String(), radix = converterUiState.value.numberSystemCustom.radix),
        )
    }

    private fun resetErrors() {
        _converterUiState.value = converterUiState.value.copy(
            numberSystem2 = _converterUiState.value.numberSystem2.copy(isError = false),
            numberSystem8 = _converterUiState.value.numberSystem8.copy(isError = false),
            numberSystem10 = _converterUiState.value.numberSystem10.copy(isError = false),
            numberSystem16 = _converterUiState.value.numberSystem16.copy(isError = false),
            numberSystemCustom = _converterUiState.value.numberSystemCustom.copy(isError = false),
        )
    }

}
