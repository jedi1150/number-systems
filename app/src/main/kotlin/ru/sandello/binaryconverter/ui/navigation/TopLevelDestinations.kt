package ru.sandello.binaryconverter.ui.navigation

import ru.sandello.binaryconverter.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val iconId: Int,
    val titleTextId: Int,
    val route: KClass<*>,
) {
    CONVERTER(
        iconId = R.drawable.ic_converter,
        titleTextId = R.string.screen_converter,
        route = ConverterRoute::class,
    ),
    CALCULATOR(
        iconId = R.drawable.ic_calculator,
        titleTextId = R.string.screen_calculator,
        route = CalculatorRoute::class,
    ),
    SETTINGS(
        iconId = R.drawable.ic_settings,
        titleTextId = R.string.screen_settings,
        route = SettingsRoute::class,
    ),
}
