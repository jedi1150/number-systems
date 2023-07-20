package ru.sandello.binaryconverter.ui.navigation

import ru.sandello.binaryconverter.R

enum class TopLevelDestination(
    val iconId: Int,
    val titleTextId: Int,
) {
    CONVERTER(
        iconId = R.drawable.ic_converter,
        titleTextId = R.string.screen_converter,
    ),
    CALCULATOR(
        iconId = R.drawable.ic_calculator,
        titleTextId = R.string.screen_calculator,
    ),
    /* SETTINGS */
}