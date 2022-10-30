package ru.sandello.binaryconverter.ui.explanation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    var explanationUiState by mutableStateOf(ExplanationUiState(state = ExplanationState.Calculating))
        private set

    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        explanationUiState = ExplanationUiState(ExplanationState.Calculating)
        explanationUiState = ExplanationUiState(ExplanationState.Completed, from, to)
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

                var finalFrom: NumberSystem = explanationUiState.from
                var finalTo: NumberSystem = explanationUiState.to

                when (explanationOperandType) {
                    ExplanationOperandType.OperandCustom1 -> finalFrom = convertedData.result
                    ExplanationOperandType.OperandCustom2 -> finalTo = convertedData.result
                }
                explanationUiState = ExplanationUiState(state = ExplanationState.Completed, finalFrom, finalTo)
            }
        }
    }

    fun updateRadix(explanationRadixType: ExplanationRadixType, newRadix: Radix) {
        explanationUiState = explanationUiState.copy(state = ExplanationState.Calculating)

        when (explanationRadixType) {
            ExplanationRadixType.RadixCustom1 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: from.radix from ${explanationUiState.from.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom1, from = explanationUiState.from, toRadixes = arrayOf(newRadix))
            }
            ExplanationRadixType.RadixCustom2 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: to.radix from ${explanationUiState.to.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom2, from = explanationUiState.to, toRadixes = arrayOf(newRadix))
            }
        }
    }

    fun swapRadixes() {
        if (explanationUiState.state is ExplanationState.Calculating) return
        Log.d(APP_TAG, "ExplanationViewModel::swapRadixes")

        updateRadix(explanationRadixType = ExplanationRadixType.RadixCustom1, newRadix = explanationUiState.to.radix)
        updateRadix(explanationRadixType = ExplanationRadixType.RadixCustom2, newRadix = explanationUiState.from.radix)
    }

}
