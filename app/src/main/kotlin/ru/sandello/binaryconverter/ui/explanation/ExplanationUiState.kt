package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import numsys.model.Radix
import ru.sandello.binaryconverter.model.NumberSystem

data class ExplanationUiState(
    val state: ExplanationState,
    val from: NumberSystem = NumberSystem(String(), Radix.DEC),
    val to: NumberSystem = NumberSystem(String(), Radix.BIN),
) {
    @SuppressLint("Range")
    val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }
}
