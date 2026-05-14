package ru.sandello.binaryconverter.ui.converter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.converter.ConverterRoute as ConverterRouteScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.navigation.ConverterRoute

fun NavController.navigateToConverter(navOptions: NavOptions? = null) {
    this.navigate(ConverterRoute, navOptions)
}

fun NavGraphBuilder.converterScreen(contentPadding: PaddingValues, viewModel: ConverterViewModel) {
    composable<ConverterRoute> {
        ConverterRouteScreen(contentPadding, viewModel = viewModel)
    }
}
