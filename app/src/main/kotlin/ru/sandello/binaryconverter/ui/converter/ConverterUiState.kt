package ru.sandello.binaryconverter.ui.converter

import android.annotation.SuppressLint
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix

data class ConverterUiState(
    val numberSystem2: NumberSystem = NumberSystem(String(), Radix.BIN),
    val numberSystem8: NumberSystem = NumberSystem(String(), Radix.OCT),
    val numberSystem10: NumberSystem = NumberSystem(String(), Radix.DEC),
    val numberSystem16: NumberSystem = NumberSystem(String(), Radix.HEX),
    val numberSystemCustom: NumberSystem = NumberSystem(String(), Radix(3)),
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = (2..36).map { Radix(it) }

    val hasData: Boolean
        get() = numberSystem10.value.isNotBlank() || numberSystem2.value.isNotBlank() || numberSystem8.value.isNotBlank() || numberSystem16.value.isNotBlank() || numberSystemCustom.value.isNotBlank()
}
