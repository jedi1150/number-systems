package ru.sandello.binaryconverter.ui.navigation

import androidx.navigation3.runtime.NavKey
import ru.sandello.binaryconverter.R

enum class TopLevelDestination(
    val iconId: Int,
    val titleTextId: Int,
    val route: NavKey,
) {
    CONVERTER(
        iconId = R.drawable.ic_converter,
        titleTextId = R.string.screen_converter,
        route = ConverterRoute,
    ),
    CALCULATOR(
        iconId = R.drawable.ic_calculator,
        titleTextId = R.string.screen_calculator,
        route = CalculatorRoute,
    ),
    SETTINGS(
        iconId = R.drawable.ic_settings,
        titleTextId = R.string.screen_settings,
        route = SettingsRoute,
    ),
}
