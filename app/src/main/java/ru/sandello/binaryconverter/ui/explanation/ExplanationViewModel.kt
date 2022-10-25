package ru.sandello.binaryconverter.ui.explanation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import ru.sandello.binaryconverter.utils.Converter
import javax.inject.Inject

enum class ExplanationOperandType { OperandCustom1, OperandCustom2 }
enum class ExplanationRadixType { RadixCustom1, RadixCustom2 }

@HiltViewModel
class ExplanationViewModel @Inject constructor(private val converter: Converter) : ViewModel() {
    private val _explanationUiState = mutableStateOf<ExplanationUiState>(ExplanationUiState.Calculating)
    val explanationUiState: State<ExplanationUiState> = _explanationUiState

    private val _nsFrom = mutableStateOf(NumberSystem(String(), Radix.DEC))
    private val _nsTo = mutableStateOf(NumberSystem(String(), Radix.BIN))


    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        _explanationUiState.value = ExplanationUiState.Calculating
        _nsFrom.value = from
        _nsTo.value = to
        _explanationUiState.value = ExplanationUiState.Complete(_nsFrom.value, _nsTo.value)
    }

    @OptIn(FlowPreview::class)
    private fun convert(explanationOperandType: ExplanationOperandType, from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "ExplanationViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        check(
            from.value.matches(
                CharRegex().charsRegex(
                    index = from.radix.value,
                    useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = from.value.count { it.toString().contains("[-]".toRegex()) } <= 1,
                ),
            ),
        ) {
            Log.d(APP_TAG, "ExplanationViewModel::convert: Invalid character entered")
            // TODO(oleg): Add crashlytics report
            return
        }


        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes.asFlow().flatMapMerge { _toRadix -> converter(from = from, toRadix = _toRadix) }.onCompletion { cause ->
                if (cause != null) {
                    Log.e(APP_TAG, "ExplanationViewModel::Flow completed exceptionally: $cause")
                    // TODO(oleg): Add crashlytics report
                }
            }.catch { error ->
                Log.e(APP_TAG, "ExplanationViewModel::convert: catch", error)
                // TODO(oleg): Add crashlytics report
            }.collect { convertedData ->
                Log.d(APP_TAG, "ExplanationViewModel::collect: operandType: $explanationOperandType, result: ${convertedData.result}")
                when (explanationOperandType) {
                    ExplanationOperandType.OperandCustom1 -> _nsFrom.value = convertedData.result
                    ExplanationOperandType.OperandCustom2 -> _nsTo.value = convertedData.result
                }
                _explanationUiState.value = ExplanationUiState.Complete(_nsFrom.value, _nsTo.value)
            }
        }
    }

    fun updateRadix(explanationRadixType: ExplanationRadixType, newRadix: Radix) {
        _explanationUiState.value = ExplanationUiState.Calculating

        when (explanationRadixType) {
            ExplanationRadixType.RadixCustom1 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: nsFrom.radix from ${_nsFrom.value.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom1, from = _nsFrom.value, toRadixes = arrayOf(newRadix))
            }
            ExplanationRadixType.RadixCustom2 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: nsTo.radix from ${_nsTo.value.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom2, from = _nsTo.value, toRadixes = arrayOf(newRadix))
            }
        }
    }

    fun swapRadixes() {
        if (_explanationUiState.value is ExplanationUiState.Calculating) return
        Log.d(APP_TAG, "ExplanationViewModel::swapRadixes")

        val radixFrom: Radix = _nsFrom.value.radix
        val radixTo: Radix = _nsTo.value.radix

        updateRadix(explanationRadixType = ExplanationRadixType.RadixCustom1, newRadix = radixTo)
        updateRadix(explanationRadixType = ExplanationRadixType.RadixCustom2, newRadix = radixFrom)
    }

}
