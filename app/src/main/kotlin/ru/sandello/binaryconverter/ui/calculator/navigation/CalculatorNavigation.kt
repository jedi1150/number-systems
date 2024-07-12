package ru.sandello.binaryconverter.ui.calculator.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.calculator.CalculatorRoute
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel

const val calculatorRoute = "calculator"

fun NavController.navigateToCalculator(navOptions: NavOptions? = null) {
    this.navigate(calculatorRoute, navOptions)
}

fun NavGraphBuilder.calculatorScreen(
    contentPadding: PaddingValues,
    viewModel: CalculatorViewModel,
) {
    composable(
        route = calculatorRoute,
    ) {
        CalculatorRoute(contentPadding, viewModel = viewModel)
    }
}
