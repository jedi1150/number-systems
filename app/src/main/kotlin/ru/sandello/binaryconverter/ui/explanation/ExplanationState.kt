package ru.sandello.binaryconverter.ui.explanation

sealed class ExplanationState {
    data object Calculating : ExplanationState()
    data object Completed : ExplanationState()
}
