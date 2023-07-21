package ru.sandello.binaryconverter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
    appState: NumSysAppState,
    modifier: Modifier = Modifier,
    converterViewModel: ConverterViewModel = hiltViewModel(),
    calculatorViewModel: CalculatorViewModel = hiltViewModel(),
    startDestination: String = converterRoute,
) {
    NavHost(
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        converterScreen(viewModel = converterViewModel)
        calculatorScreen(viewModel = calculatorViewModel)
        settingsScreen()
    }
}
