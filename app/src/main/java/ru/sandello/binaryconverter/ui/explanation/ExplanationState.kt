package ru.sandello.binaryconverter.ui.explanation

sealed class ExplanationState {
    object Calculating : ExplanationState()
    object Completed : ExplanationState()
}
