package ru.sandello.binaryconverter.ui.main

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Shared

class MainViewModel : ViewModel() {
    private val _explanationState = MutableStateFlow<ExplanationState>(ExplanationState.Calculating)
    val explanationState: StateFlow<ExplanationState> = _explanationState

    private val _nsFrom = mutableStateOf(NumberSystem(String(), Radix(10)))
    private val _nsTo = mutableStateOf(NumberSystem(String(), Radix(2)))

    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        _explanationState.value = ExplanationState.Calculating
        _nsFrom.value = from
        _nsTo.value = to
        _explanationState.value = ExplanationState.Complete(_nsFrom.value, _nsTo.value)
    }

    fun convertFrom(from: NumberSystem) {
        convert(from, toRadixes = arrayOf(_nsTo.value.radix))
    }

    @OptIn(FlowPreview::class)
    private fun convert(from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "MainViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        if (from.value.isEmpty()) {
//            clear()
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
            Log.d(APP_TAG, "MainViewModel::convert: Invalid character entered")
            when (from.radix) {
//                _numberSystem2.value.radix -> _numberSystem2error.value = true
            }
            return
        }


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
//                                _numberSystem2.value.radix -> {
//                                    if (_numberSystem2.value.value == from.value) cancel()
//                                    _numberSystem2.value = tempValue
//                                }
                            }
                        }
                    }
                }
                .asFlow()
                .flatMapMerge { _toRadix -> Shared.converter(from = tempValue, toRadix = _toRadix) }
                .onCompletion { cause ->
                    if (cause != null) {
                        Log.e(APP_TAG, "Flow completed exceptionally: $cause")
                    } else {
//                        resetErrors()
                    }
                }
                .catch { error -> Log.e(APP_TAG, "MainViewModel::convert: catch", error) }
                .collect { convertedData ->
                    _nsTo.value = convertedData.result
                }
        }
    }

}
