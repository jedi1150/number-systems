package ru.sandello.binaryconverter.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.sandello.binaryconverter.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int,
) {
    object Converter : Screen("converter", R.string.Converter, R.drawable.ic_converter)
    object Calculator : Screen("calculator", R.string.Calculator, R.drawable.ic_calculator)
}
