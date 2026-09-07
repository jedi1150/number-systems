package ru.sandello.binaryconverter.ui.converter.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import ru.sandello.binaryconverter.ui.converter.ConverterRoute as ConverterRouteScreen
import ru.sandello.binaryconverter.ui.converter.ConverterViewModel
import ru.sandello.binaryconverter.ui.navigation.ConverterRoute

fun EntryProviderScope<NavKey>.converterScreen(contentPadding: PaddingValues, viewModel: ConverterViewModel) {
    entry<ConverterRoute> {
        ConverterRouteScreen(contentPadding, viewModel = viewModel)
    }
}
