package ru.sandello.binaryconverter.model

import androidx.annotation.StringRes
import ru.sandello.binaryconverter.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Converter : Screen("converter", R.string.Converter)
    object Calculator : Screen("calculator", R.string.Calculator)
}
