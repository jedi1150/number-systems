package ru.sandello.binaryconverter.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.explanation.ExplanationViewModel
import ru.sandello.binaryconverter.ui.main.MainRoute

@Composable
fun NumberSystemsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "numsys") {
        composable("numsys") {

            val converterViewModel = hiltViewModel<ConverterViewModel>()
            val calculatorViewModel = hiltViewModel<CalculatorViewModel>()
            val explanationViewModel = hiltViewModel<ExplanationViewModel>()

            MainRoute(
                converterViewModel = converterViewModel,
                calculatorViewModel = calculatorViewModel,
                explanationViewModel = explanationViewModel,
            )
        }
        composable("settings") {}
    }
}
