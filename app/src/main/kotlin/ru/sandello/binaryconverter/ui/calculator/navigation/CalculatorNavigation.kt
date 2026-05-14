package ru.sandello.binaryconverter.ui.calculator.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.calculator.CalculatorRoute as CalculatorRouteScreen
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.navigation.CalculatorRoute

fun NavController.navigateToCalculator(navOptions: NavOptions? = null) {
    this.navigate(CalculatorRoute, navOptions)
}

fun NavGraphBuilder.calculatorScreen(
    contentPadding: PaddingValues,
    viewModel: CalculatorViewModel,
) {
    composable<CalculatorRoute> {
        CalculatorRouteScreen(contentPadding, viewModel = viewModel)
    }
}
