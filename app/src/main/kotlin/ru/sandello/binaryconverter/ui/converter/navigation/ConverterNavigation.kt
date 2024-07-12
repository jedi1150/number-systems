package ru.sandello.binaryconverter.ui.converter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.sandello.binaryconverter.ui.converter.ConverterRoute
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel

const val converterRoute = "converter"

fun NavController.navigateToConverter(navOptions: NavOptions? = null) {
    this.navigate(converterRoute, navOptions)
}

fun NavGraphBuilder.converterScreen(contentPadding: PaddingValues, viewModel: ConverterViewModel) {
    composable(
        route = converterRoute,
    ) {
        ConverterRoute(contentPadding, viewModel = viewModel)
    }
}
