package ru.sandello.binaryconverter.ui.explanation

import android.annotation.SuppressLint
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix

sealed class ExplanationUiState {
    object Calculating : ExplanationUiState()
    data class Complete(
        val from: NumberSystem,
        val to: NumberSystem,
    ) : ExplanationUiState() {
        @SuppressLint("Range")
        val radixes: List<Radix> = Array(36) { radix -> Radix(radix + 1) }.filter { radix -> !listOf(Radix(1)).contains(radix) }
    }
}
