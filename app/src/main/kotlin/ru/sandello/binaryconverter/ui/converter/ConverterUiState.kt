package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import numsys.model.Radix
import ru.sandello.binaryconverter.model.NumberSystem

data class ConverterUiState(
    val numberSystem2: NumberSystem = NumberSystem(String(), Radix.BIN),
    val numberSystem8: NumberSystem = NumberSystem(String(), Radix.OCT),
    val numberSystem10: NumberSystem = NumberSystem(String(), Radix.DEC),
    val numberSystem16: NumberSystem = NumberSystem(String(), Radix.HEX),
    val numberSystemCustom: NumberSystem = NumberSystem(String(), Radix(3)),
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1), numberSystem2.radix, numberSystem8.radix, numberSystem10.radix, numberSystem16.radix).contains(radix) }

    val hasData: Boolean
        get() = numberSystem10.value.isNotBlank() || numberSystem2.value.isNotBlank() || numberSystem8.value.isNotBlank() || numberSystem16.value.isNotBlank() || numberSystemCustom.value.isNotBlank()
}
