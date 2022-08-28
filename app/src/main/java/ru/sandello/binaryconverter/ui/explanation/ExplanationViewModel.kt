package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.State
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

enum class OperandType {
    OperandCustom1,
    OperandCustom2,
}

enum class RadixType {
    RadixCustom1,
    RadixCustom2,
}

class ExplanationViewModel : ViewModel() {
    private val _explanationState = MutableStateFlow<ExplanationState>(ExplanationState.Calculating)
    val explanationState: StateFlow<ExplanationState> = _explanationState

    private val _nsFrom = mutableStateOf(NumberSystem(String(), Radix(10)))
    val nsFrom: State<NumberSystem> = _nsFrom
    private val _nsTo = mutableStateOf(NumberSystem(String(), Radix(2)))
    val nsTo: State<NumberSystem> = _nsTo

    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }

    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        _explanationState.value = ExplanationState.Calculating
        _nsFrom.value = from
        _nsTo.value = to
        _explanationState.value = ExplanationState.Complete(_nsFrom.value, _nsTo.value)
    }

    @OptIn(FlowPreview::class)
    private fun convert(operandType: OperandType, from: NumberSystem, toRadixes: Array<Radix>) {
        Log.d(APP_TAG, "ExplanationViewModel::convert: value: ${from.value}, from radix: ${from.radix.value}")

        check(
            from.value.matches(
                CharRegex().charsRegex(
                    index = from.radix.value,
                    useDelimiterChars = from.value.count { it.toString().contains("[,.]".toRegex()) } <= 1,
                    useNegativeChar = from.value.count { it.toString().contains("[-]".toRegex()) } <= 1,
                )
            )
        ) {
            Log.d(APP_TAG, "ExplanationViewModel::convert: Invalid character entered")
            // TODO(oleg): Add crashlytics report
            return
        }


        if (from.value.contains("-".toRegex())) {
            from.value = from.value.replace("-", "").replaceRange(0, 0, "-")
        }

        viewModelScope.launch {
            toRadixes
                .asFlow()
                .flatMapMerge { _toRadix -> Shared.converter(from = from, toRadix = _toRadix) }
                .onCompletion { cause ->
                    if (cause != null) {
                        Log.e(APP_TAG, "ExplanationViewModel::Flow completed exceptionally: $cause")
                        // TODO(oleg): Add crashlytics report
                    }
                }
                .catch { error ->
                    Log.e(APP_TAG, "ExplanationViewModel::convert: catch", error)
                    // TODO(oleg): Add crashlytics report
                }
                .collect { convertedData ->
                    Log.d(APP_TAG, "ExplanationViewModel::collect: operandType: $operandType, result: ${convertedData.result}")
                    when (operandType) {
                        OperandType.OperandCustom1 -> _nsFrom.value = convertedData.result
                        OperandType.OperandCustom2 -> _nsTo.value = convertedData.result
                    }
                    _explanationState.value = ExplanationState.Complete(_nsFrom.value, _nsTo.value)
                }
        }
    }

    fun updateRadix(radixType: RadixType, newRadix: Radix) {
        _explanationState.value = ExplanationState.Calculating

        when (radixType) {
            RadixType.RadixCustom1 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: nsFrom.radix from ${_nsFrom.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandType.OperandCustom1, from = _nsFrom.value, toRadixes = arrayOf(newRadix))
            }
            RadixType.RadixCustom2 -> {
                Log.d(APP_TAG, "ExplanationViewModel::updateRadix: nsTo.radix from ${_nsTo.value.radix.value} to ${newRadix.value}")
                convert(operandType = OperandType.OperandCustom2, from = _nsTo.value, toRadixes = arrayOf(newRadix))
            }
        }
    }

    fun radixesViceVersa() {
        Log.d(APP_TAG, "ExplanationViewModel::radixesViceVersa")
        updateRadix(radixType = RadixType.RadixCustom1, newRadix = _nsTo.value.radix)
        updateRadix(radixType = RadixType.RadixCustom2, newRadix = _nsFrom.value.radix)
    }

}
