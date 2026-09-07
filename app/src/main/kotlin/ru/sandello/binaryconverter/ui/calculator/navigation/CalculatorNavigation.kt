package ru.sandello.binaryconverter.ui.calculator.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.calculator.CalculatorRoute as CalculatorRouteScreen
import ru.sandello.binaryconverter.ui.navigation.CalculatorRoute

fun EntryProviderScope<NavKey>.calculatorScreen(
    contentPadding: PaddingValues,
    viewModel: CalculatorViewModel,
) {
    entry<CalculatorRoute> {
        CalculatorRouteScreen(contentPadding, viewModel = viewModel)
    }
}
