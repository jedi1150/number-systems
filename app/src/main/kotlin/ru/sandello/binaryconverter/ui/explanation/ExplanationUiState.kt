package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.numsys.model.Radix

data class ExplanationUiState(
    val state: ExplanationState,
    val from: NumberSystem = NumberSystem(String(), Radix.DEC),
    val to: NumberSystem = NumberSystem(String(), Radix.BIN),
) {
    val radixes: List<Radix> = (2..36).map { Radix(it) }
}
