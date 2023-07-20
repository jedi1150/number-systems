package ru.sandello.binaryconverter.ui.explanation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import numsys.NumSys
import numsys.model.NumberSystem
import numsys.model.Radix
import ru.sandello.binaryconverter.utils.APP_TAG
import ru.sandello.binaryconverter.utils.CharRegex
import javax.inject.Inject

enum class ExplanationOperandType { OperandCustom1, OperandCustom2 }
enum class ExplanationRadixType { RadixCustom1, RadixCustom2 }

@HiltViewModel
class ExplanationViewModel @Inject constructor(private val numSys: NumSys) : ViewModel() {

    private val _explanationUiState: MutableStateFlow<ExplanationUiState> = MutableStateFlow(ExplanationUiState(state = ExplanationState.Calculating))
    val explanationUiState: StateFlow<ExplanationUiState> = _explanationUiState.asStateFlow()

    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        _explanationUiState.value = ExplanationUiState(ExplanationState.Calculating)
        _explanationUiState.value = ExplanationUiState(ExplanationState.Completed, from, to)
    }

    private fun convert(explanationOperandType: ExplanationOperandType, from: NumberSystem, toRadix: Radix) {
        Log.d(APP_TAG, "ExplanationViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        check(
            from.value.matches(
                CharRegex().charsRegex(
                    index = from.radix.value,
                    useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = from.value.count { it.toString().contains("-".toRegex()) } <= 1,
                ),
            ),
        ) {
            Log.w(APP_TAG, "ExplanationViewModel::convert: Invalid character entered")
            // TODO(oleg): Add crashlytics report
            return
        }


        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            numSys.convert(value = from, toRadix = toRadix).let { convertedData ->
                var finalFrom: NumberSystem = explanationUiState.value.from
                var finalTo: NumberSystem = explanationUiState.value.to

                when (explanationOperandType) {
                    ExplanationOperandType.OperandCustom1 -> finalFrom = convertedData
                    ExplanationOperandType.OperandCustom2 -> finalTo = convertedData
                }
                _explanationUiState.value = ExplanationUiState(state = ExplanationState.Completed, finalFrom, finalTo)
            }
        }
    }

    fun updateRadix(explanationRadixType: ExplanationRadixType, newRadix: Radix) {
        _explanationUiState.value = explanationUiState.value.copy(state = ExplanationState.Calculating)

        when (explanationRadixType) {
            ExplanationRadixType.RadixCustom1 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: from.radix from ${explanationUiState.value.from.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom1, from = explanationUiState.value.from, toRadix = newRadix)
            }

            ExplanationRadixType.RadixCustom2 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: to.radix from ${explanationUiState.value.to.radix.value} to ${newRadix.value}")
                convert(explanationOperandType = ExplanationOperandType.OperandCustom2, from = explanationUiState.value.to, toRadix = newRadix)
            }
        }
    }

}
