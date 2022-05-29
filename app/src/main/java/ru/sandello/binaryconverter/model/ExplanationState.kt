package ru.sandello.binaryconverter.model

sealed class ExplanationState {
    object Calculating : ExplanationState()
    data class Complete(val from: NumberSystem, val to: NumberSystem) : ExplanationState()
}
