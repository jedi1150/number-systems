package ru.sandello.binaryconverter.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import ru.sandello.binaryconverter.ui.NumSysAppState
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.calculator.navigation.calculatorScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.converter.navigation.converterRoute
import ru.sandello.binaryconverter.ui.converter.navigation.converterScreen
import ru.sandello.binaryconverter.ui.settings.navigation.settingsScreen

@Composable
fun NumSysNavHost(
    contentPadding: PaddingValues,
    appState: NumSysAppState,
    converterViewModel: ConverterViewModel = hiltViewModel(),
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    startDestination: String = converterRoute,
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        converterScreen(contentPadding, viewModel = converterViewModel)
        calculatorScreen(contentPadding, viewModel = calculatorViewModel)
        settingsScreen(contentPadding)
    }
}
