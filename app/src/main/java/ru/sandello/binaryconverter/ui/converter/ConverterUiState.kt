package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix

data class ConverterUiState(
    val numberSystem10: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.DEC)),
    val numberSystem2: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.BIN)),
    val numberSystem8: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.OCT)),
    val numberSystem16: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix.HEX)),
    val numberSystemCustom: State<NumberSystem> = mutableStateOf(NumberSystem(String(), Radix(3))),
    val numberSystem10error: State<Boolean> = mutableStateOf(false),
    val numberSystem2error: State<Boolean> = mutableStateOf(false),
    val numberSystem8error: State<Boolean> = mutableStateOf(false),
    val numberSystem16error: State<Boolean> = mutableStateOf(false),
    val numberSystemCustomError: State<Boolean> = mutableStateOf(false),
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1), numberSystem2.value.radix, numberSystem8.value.radix, numberSystem10.value.radix, numberSystem16.value.radix).contains(radix) }

    val hasData: State<Boolean>
        get() = mutableStateOf(numberSystem10.value.value.isNotBlank() || numberSystem2.value.value.isNotBlank() || numberSystem8.value.value.isNotBlank() || numberSystem16.value.value.isNotBlank() || numberSystemCustom.value.value.isNotBlank())
}
