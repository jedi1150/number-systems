package ru.sandello.binaryconverter.ui.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.sandello.binaryconverter.model.ExplanationState
import ru.sandello.binaryconverter.model.NumberSystem
import ru.sandello.binaryconverter.model.Radix

class MainViewModel : ViewModel() {
    private val _explanationState = MutableStateFlow(ExplanationState.Calculating)
    val explanationState: StateFlow<ExplanationState> = _explanationState

    private val _nsFrom = mutableStateOf(NumberSystem(String(), Radix(10)))
    val nsFrom: State<NumberSystem> = _nsFrom
    private val _nsTo = mutableStateOf(NumberSystem(String(), Radix(2)))
    val nsTo: State<NumberSystem> = _nsTo

    fun acceptValues(from: NumberSystem, to: NumberSystem) {
        _nsFrom.value = from
        _nsTo.value = to
    }

}
