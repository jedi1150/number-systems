package ru.sandello.binaryconverter.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object ConverterRoute : NavKey

@Serializable
data object CalculatorRoute : NavKey

@Serializable
data object SettingsRoute : NavKey
