package ru.sandello.binaryconverter.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import ru.sandello.binaryconverter.ui.calculator.CalculatorViewModel
import ru.sandello.binaryconverter.ui.calculator.navigation.calculatorScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.converter.navigation.converterScreen
import ru.sandello.binaryconverter.ui.settings.navigation.settingsScreen

@Composable
fun NumSysNavHost(
    contentPadding: PaddingValues,
    navigationState: NavigationState,
    navigator: Navigator,
    converterViewModel: ConverterViewModel,
    calculatorViewModel: CalculatorViewModel,
) {
    val entryProvider = entryProvider {
        converterScreen(contentPadding, viewModel = converterViewModel)
        calculatorScreen(contentPadding, viewModel = calculatorViewModel)
        settingsScreen(contentPadding)
    }

    val fadeThrough = fadeIn() togetherWith fadeOut()

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        transitionSpec = { fadeThrough },
        popTransitionSpec = { fadeThrough },
        predictivePopTransitionSpec = { fadeThrough },
    )
}
